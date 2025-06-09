using System;
using StackExchange.Redis;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

public static void Run(string myEventHubMessage,TraceWriter log)
{
    //get jObject
    JObject jObject=JObject.Parse(myEventHubMessage);
    //get roadConditionId from jObject
    string roadConditionId=jObject
        .Value<JObject>("data")
        .Value<JObject>("properties")
        .Value<string>("RoadConditionId");
    log.Info($"Received event with roadConditionId {roadConditionId}");

    //get connection
    var connection=ConnectionMultiplexer.Connect("${redis.host}:${redis.port},password=${redis.access.key},ssl=${redis.use.ssl},abortConnect=False");
    IDatabase db=connection.GetDatabase(${redis.road-conditions.database.number});
    log.Info($"Connected to database {db}");

    //push event into Redis hashMap
    var hashMap="${redis.road-conditions.hashmap.name}";
    db.HashSet(hashMap,roadConditionId,JsonConvert.SerializeObject(jObject),When.Always,CommandFlags.None);

    log.Info($"Object with key {roadConditionId} pushed into Redis hashMap {hashMap}.");
}