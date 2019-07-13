using System.Collections.Generic;
using Newtonsoft.Json;

namespace ServiceA.Models {
    
    public class TraceRequestModel {

        [JsonProperty("options")]
        public List<ServiceOptions> Options {get;set;}

    
    }

    public class ServiceOptions {

        [JsonProperty("serviceName")]
        public string ServiceName {get;set;}
        [JsonProperty("throwException")]
        public bool ThrowException {get;set;}
        [JsonProperty("delay")]
        public int Delay {get;set;}
    }

}