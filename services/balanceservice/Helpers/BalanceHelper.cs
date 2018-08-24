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
                var dr = producer.ProduceAsync("balance-events", null, "test message text").Result;
                Console.WriteLine($"Delivered '{dr.Value}' to: {dr.TopicPartitionOffset}");
            }
        }
    }
}