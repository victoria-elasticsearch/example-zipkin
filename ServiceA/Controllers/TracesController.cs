using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
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

         private readonly Microsoft.Extensions.Logging.ILogger _logger;
         public IConfiguration _configuration { get; }
        
        public TracesController(ILoggerFactory loggerFactory, IConfiguration configuration) {
            _logger = loggerFactory.CreateLogger(nameof(TracesController));
            _configuration = configuration;
        }

        
        // POST api/values
        [HttpPost]
        public async Task<ActionResult<string>> Post([FromBody]TraceRequestModel traceRequestModel)
        {
            _logger.LogInformation("Trace POST");
            var options = traceRequestModel.Options.FirstOrDefault(opt => String.Equals("ServiceA", opt.ServiceName, StringComparison.OrdinalIgnoreCase));

            if (options == null) {
                _logger.LogInformation("no options");
                return Ok("Posted");
            }

            if(options.ThrowException) {
                _logger.LogInformation("service will throw exception");
                throw new Exception("This should be a custom exception");
            }

            if(options.Delay > 0) {
                _logger.LogInformation($"service sleep: {options.Delay}");
                Thread.Sleep(options.Delay);
            }

            using(var client = new HttpClient()) {

                var serviceBUrl = $"{_configuration.GetSection("SERVICEB_URL").Value ?? "http://localhost:8080"}/trace";


                var result = await client.PostAsJsonAsync(serviceBUrl, traceRequestModel);

                _logger.LogInformation($"Service B Status: {result.StatusCode}");
            }

            
            return Ok(traceRequestModel);
     
        }
    }
}
