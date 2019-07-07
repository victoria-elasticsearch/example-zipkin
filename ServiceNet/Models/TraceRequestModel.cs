using System.Collections.Generic;

namespace ServiceA.Models {
    
    public class TraceRequestModel {

        public List<ServiceOptions> Options {get;set;}

    
    }

    public class ServiceOptions {

        public string ServiceName {get;set;}
        public bool ThrowException {get;set;}
        public int Delay {get;set;}
    }

}