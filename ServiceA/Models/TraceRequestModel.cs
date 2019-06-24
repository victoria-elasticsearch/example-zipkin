namespace ServiceA.Models {
    
    public class TraceRequestModel {

        public ServiceOptions ServiceAOptions {get;set;}

    
    }

    public class ServiceOptions {
        public bool ThrowException {get;set;}
        public int Delay {get;set;}
    }

}