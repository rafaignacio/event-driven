using System.Collections.Generic;
using balanceservice.Models;
using Microsoft.AspNetCore.Mvc;
using balanceservice.Helpers;

namespace balanceservice.Controllers
{
    [Route("balance")]
    public class BalanceController : Controller
    {
        [Route("{id}")]
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

        [HttpPost]
        [Route("account/{user_id}")]
        public IActionResult CreateAccount(int user_id)
        {
            BalanceHelper.CreateBalanceAccount(user_id);
            return Created("account", user_id);
        }
    }
}