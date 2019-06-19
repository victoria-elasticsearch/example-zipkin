using System;
using System.Collections.Generic;
using System.Linq;
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
                    ThrowExceptionInServiceA = false
                }
            };
            return Ok(result);
        }

        // GET api/values/5
        [HttpGet("{id}")]
        public ActionResult<string> Get(int id)
        {
            return "value";
        }

        // POST api/values
        [HttpPost]
        public ActionResult<string> Post([FromBody]TraceRequestModel traceRequestModel)
        {

            if(traceRequestModel.ThrowExceptionInServiceA) throw new Exception("This should be a custom exception");
            
            return Ok("Posted");
     
        }

        // PUT api/values/5
        [HttpPut("{id}")]
        public void Put(int id, [FromBody] string value)
        {
        }

        // DELETE api/values/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {
        }
    }
}
