using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using ServiceA.Models;
using zipkin4net;
using zipkin4net.Middleware;
using zipkin4net.Tracers.Zipkin;
using zipkin4net.Transport.Http;

namespace ServiceA.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class TracesController : ControllerBase
    {

         private readonly ILoggerFactory _loggerFactory;
        
        public TracesController(ILoggerFactory loggerFactory) {
            _loggerFactory = loggerFactory;
        }


        // GET api/values
        [HttpGet]
        public ActionResult<IEnumerable<TraceRequestModel>> Get() {

            var result = new List<TraceRequestModel>() {
                new TraceRequestModel() {
                    ServiceAOptions = new ServiceOptions {
                        ThrowException = false,
                        Delay = 10
                    }
                }
            };
            return Ok(result);
        }
        
        // POST api/values
        [HttpPost]
        public ActionResult<string> Post([FromBody]TraceRequestModel traceRequestModel)
        {

            if (traceRequestModel.ServiceAOptions == null) 
                return Ok("Posted");

            if(traceRequestModel.ServiceAOptions.ThrowException) 
                throw new Exception("This should be a custom exception");
            
            if(traceRequestModel.ServiceAOptions.Delay > 0)
                Thread.Sleep(traceRequestModel.ServiceAOptions.Delay);

            return Ok("Posted");
     
        }
    }
}
