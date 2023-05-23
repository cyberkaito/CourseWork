using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using PolyphoniaWeb.Models;
using System.Security.Claims;
using Microsoft.AspNetCore.Authentication.Cookies;
using static Azure.Core.HttpHeader;
using System.Net.Mail;
using System.Net;
using Microsoft.AspNetCore.Identity;
using System.Text;

namespace PolyphoniaWeb.Controllers
{
    public class LoginupController : Controller
    {
        private PolyphoniaDatabaseContext db;
        public LoginupController(PolyphoniaDatabaseContext context)
        {
            db = context;
        }
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> LogUp(List<string> data)
        {
            Client client = new Client();
            client.Name = data[0];
            client.Email = data[1];
            client.Key = data[2];
            HttpContext.Session.SetString("AuthUser", client.Email);
            await Authenticate(client.Email);
            db.Clients.Add(client);
            await db.SaveChangesAsync();
            
            return Redirect("~/Home/Index");
        }
        public static string GetRandomPassword()
        {
            const string chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

            StringBuilder sb = new StringBuilder();
            Random rnd = new Random();

            int length = rnd.Next(8, 20);
            for (int i = 0; i < length; i++)
            {
                int index = rnd.Next(chars.Length);
                sb.Append(chars[index]);
            }

            return sb.ToString();
        }
        private static async Task SendEmailAsync(string email, string key)
        {
            MailAddress from = new MailAddress("katezhukovazhukova@yandex.ru", "Polyphonia");
            MailAddress to = new MailAddress(email);
            MailMessage m = new MailMessage(from, to);
            m.Subject = "Восстановление пароля";
            
            m.Body = $"Ваш новый пароль: {key}";
            SmtpClient smtp = new SmtpClient("smtp.yandex.ru", 587);
            smtp.UseDefaultCredentials = false;
            smtp.Credentials = new NetworkCredential("katezhukovazhukova@yandex.ru", "hcpkdyjtdqkahlop");
            smtp.EnableSsl = true;
            smtp.DeliveryMethod = SmtpDeliveryMethod.Network;
            await smtp.SendMailAsync(m);
        }
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> LogIn(List<string> data)
        {
                Client client = await db.Clients.FirstOrDefaultAsync(u => u.Email == data[1] && u.Key == data[2]);
                if (client != null)
                {
                    HttpContext.Session.SetString("AuthUser", client.Email);
                    await Authenticate(client.Email);
                }
                if (db.ClientTypes.Any(n => n.IdClient == client.IdClient && n.IdRole == 3))
                {
                    return Redirect("~/Admin/Admin");
                }
            return Redirect("~/Home/Index");
        }
        private async Task Authenticate(string email)
        {
            var claims = new List<Claim>
            {
                new Claim(ClaimsIdentity.DefaultNameClaimType, email)
            };
            ClaimsIdentity id = new ClaimsIdentity(claims, "ApplicationCookie", ClaimsIdentity.DefaultNameClaimType, ClaimsIdentity.DefaultRoleClaimType);
            await HttpContext.SignInAsync(CookieAuthenticationDefaults.AuthenticationScheme, new ClaimsPrincipal(id));
        }
        public async Task<IActionResult> Logout()
        {
            await HttpContext.SignOutAsync(CookieAuthenticationDefaults.AuthenticationScheme);
            HttpContext.Session.Remove("AuthUser");
            return Redirect("~/Home/Index");
        }
    }
}
