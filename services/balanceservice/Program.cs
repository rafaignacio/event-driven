using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Confluent.Kafka;
using Confluent.Kafka.Serialization;
using Microsoft.AspNetCore;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;

namespace balanceservice
{
    public class Program
    {
        public static void Main(string[] args)
        {
            CreateConsumer().GetAwaiter();

            CreateWebHostBuilder(args).UseKestrel().UseStartup<Startup>()
            .UseUrls("http://0.0.0.0:5000")
            .Build().Run();
        }

        private static async Task CreateConsumer()
        {
            await Task.Run(() =>
            {
                var conf = new Dictionary<string, object>
                {
                    { "group.id", "balance-group-7" },
                    { "bootstrap.servers", "kafka1:9093,kafka2:9093" },
                    { "auto.offset.reset", "earliest" },
                    { "enable.auto.commit", "false" },
                };

                using (var consumer = new Consumer<Null, string>(conf, null, new StringDeserializer(Encoding.UTF8)))
                {
                    consumer.OnMessage += async (_, msg) =>
                    {
                        Console.WriteLine($"Value: {msg.Value}\r\nFrom partition: {msg.Partition}");

                        await consumer.CommitAsync(msg);
                    };

                    consumer.OnError += (_, error)
                        => Console.WriteLine($"Error: {error}");

                    consumer.OnConsumeError += (_, msg)
                        => Console.WriteLine($"Consume error ({msg.TopicPartitionOffset}): {msg.Error}");

                    consumer.Subscribe("userevent");

                    while (true)
                    {
                        consumer.Poll(TimeSpan.FromMilliseconds(100));
                    }
                }
            });
        }

        public static IWebHostBuilder CreateWebHostBuilder(string[] args) =>
            WebHost.CreateDefaultBuilder(args)
                .UseStartup<Startup>();
    }
}
