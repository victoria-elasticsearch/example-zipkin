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
        private readonly IConfiguration _configuration;
        private readonly IHttpClientFactory _httpClientFactory;


        public TracesController(
                ILoggerFactory loggerFactory,
                IConfiguration configuration,
                IHttpClientFactory httpClientFactory)
        {
            _logger = loggerFactory.CreateLogger(nameof(TracesController));
            _configuration = configuration;
            _httpClientFactory = httpClientFactory;
        }


        // POST api/values
        [HttpPost]
        public async Task<ActionResult<string>> Post([FromBody]TraceRequestModel traceRequestModel)
        {
            _logger.LogInformation("Trace POST");
            var options = traceRequestModel.Options.FirstOrDefault(opt => String.Equals("ServiceNet", opt.ServiceName, StringComparison.OrdinalIgnoreCase));

            if (options == null)
            {
                _logger.LogInformation("no options");
                return Ok("Posted");
            }

            if (options.ThrowException)
            {
                _logger.LogInformation("service will throw exception");
                throw new Exception("This should be a custom exception");
            }

            if (options.Delay > 0)
            {
                _logger.LogInformation($"service sleep: {options.Delay}");
                Thread.Sleep(options.Delay);
            }


            var serviceBUrl = $"{_configuration.GetSection("SERVICEB_URL").Value ?? "http://localhost:8080"}/trace";



            using (var httpClient = _httpClientFactory.CreateClient("Tracer"))
            {
                var response = await httpClient.PostAsJsonAsync(serviceBUrl, traceRequestModel);
                if (response.IsSuccessStatusCode)
                {
                    _logger.LogInformation($"ServiceB response: {response.ReasonPhrase}");
                }
                else
                {
                    _logger.LogCritical($"ServiceB response: {response.ReasonPhrase}");
                }
            };

            return Ok(traceRequestModel);

        }
    }
}
