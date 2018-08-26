using System;
using System.Collections.Generic;
using System.Text;
using Confluent.Kafka;
using Confluent.Kafka.Serialization;

namespace balanceservice.Helpers
{
    public static class BalanceHelper
    {
        internal static void CreateBalanceAccount(int user_id)
        {
            var config = new Dictionary<string, object>
            {
                { "bootstrap.servers", "kafka1:9093,kafka2:9093" }
            };

            using (var producer = new Producer<Null, string>(config, null, new StringSerializer(Encoding.UTF8)))
            {
                using (var data = new DataContext())
                {
                    data.Database.EnsureCreated();

                    var acc = new Models.AccountViewModel
                    {
                        ID = System.Guid.NewGuid().ToString(),
                        UserID = user_id,
                        Host = System.Environment.GetEnvironmentVariable("HOSTNAME") ?? "localhost",
                    };

                    data.Accounts.Add(acc);
                    data.SaveChanges();

                    var dr = producer.ProduceAsync("balance-events", null, $"{{ type: \"ACC_CREATED\"," +
                        $" data: {{ \"user\" : {acc.UserID}, \"acc_id\": \"{acc.ID}\" }}}}").Result;
                    Console.WriteLine($"Delivered '{dr.Value}' to: {dr.TopicPartitionOffset}");
                }
            }
        }
    }
}