using System.Collections.Generic;
using balanceservice.Models;
using Microsoft.AspNetCore.Mvc;

namespace balanceservice.Controllers
{
    [Route("balance")]
    public class BalanceController : Controller
    {
        [Route("{id:string}")]
        [HttpGet]
        public IActionResult GetBalance(string id)
        {
            BalanceViewModel output = null;

            if (output != null)
            {
                return Json(output);
            }
            else
            {
                return NotFound();
            }
        }
    }
}