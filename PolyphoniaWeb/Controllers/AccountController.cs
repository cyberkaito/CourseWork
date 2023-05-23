using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using PolyphoniaWeb.Models;
using System.IO;
using System.Linq;

namespace PolyphoniaWeb.Controllers
{
    public class AccountController : Controller
    {
        private PolyphoniaDatabaseContext db;
        public AccountController(PolyphoniaDatabaseContext context)
        {
            db = context;
        }
        // GET: AccountController
        public ActionResult Account()
        {
            var channels = db.Channels.ToList();
            var clients = db.Clients.ToList();
            var clientTypes = db.ClientTypes.ToList();
            var roles = db.Roles.ToList();
            var categories = db.Categories.ToList();
            var news = db.News.ToList();
            var media = db.Media.ToList();
            var comment = db.Comments.ToList();
            Client thisclient = db.Clients.FirstOrDefault(n => n.Email == HttpContext.Session.GetString("AuthUser"));
            if (db.ClientTypes.Any(n => n.IdClient == thisclient.IdClient && n.IdRole == 3))
            {
                return Redirect("~/Admin/Admin");
            }
            var clientData = new ClientData(thisclient.Email, thisclient.Key, thisclient.IdClient, thisclient.Name, thisclient.Avatar, thisclient.Bio);
            var model = new AccountModel(channels, clientTypes, news, media, comment, categories, clientData);
            return View(model);
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> editProfile(List<string> data)
        {
            Client client = db.Clients.FirstOrDefault(n => n.Email == HttpContext.Session.GetString("AuthUser"));
            client.Name = data[0];
            client.Email = data[1];
            client.Key = data[2];
            client.Avatar = data[3];
            client.Bio = data[4];
            db.Clients.Attach(client);
            db.Clients.Update(client);
            await db.SaveChangesAsync();
            HttpContext.Session.SetString("AuthUser", client.Email.Trim());
            return Redirect("~/Account/Account");
        }
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> addChannel(List<string> data)
        {
            Client client = db.Clients.FirstOrDefault(n => n.Email == HttpContext.Session.GetString("AuthUser"));
            Channel channel = new Channel();
            channel.Name = data[0];
            channel.Description = data[1];
            channel.Avatar = data[2];
            channel.Rate = 0.0;
            channel.RateCount = 0;
            db.Channels.Add(channel);
            await db.SaveChangesAsync();
            ClientType clientType = new ClientType();
            clientType.IdClient = client.IdClient;
            clientType.IdChannel = channel.IdChannel;
            clientType.IdRole = 2;
            db.ClientTypes.Add(clientType);
            await db.SaveChangesAsync();
            return Redirect("~/Account/Account");
        }
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> editChannel(List<string> data)
        {
            Client client = db.Clients.FirstOrDefault(n => n.Email == HttpContext.Session.GetString("AuthUser"));
            var channelID = db.ClientTypes.Where(n => n.IdClient == client.IdClient && n.IdRole == 2).ToList()[0].IdChannel;
            Channel channel = db.Channels.Where(n => n.IdChannel == channelID).ToList()[0];
            channel.Name = data[0];
            channel.Description = data[1];
            channel.Avatar = data[2];
            db.Channels.Attach(channel);
            db.Channels.Update(channel);
            await db.SaveChangesAsync();
            return Redirect("~/Account/Account");
        }
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> deleteChannel(List<string> data)
        {
            Channel channel = db.Channels.Find(Int32.Parse(data[0]));
            var News = db.News.Where(n => n.IdChannel == channel.IdChannel).ToList();
            foreach (var item in News)
            {
                var Media = db.Media.Where(n => n.IdNews == item.IdNews).ToList();
                foreach (var med in Media)
                {
                    db.Media.Attach(med);
                    db.Media.Remove(med);
                    await db.SaveChangesAsync();
                }
                var Comments = db.Comments.Where(n => n.IdNews == item.IdNews).ToList();
                foreach (var med in Comments)
                {
                    db.Comments.Attach(med);
                    db.Comments.Remove(med);
                    await db.SaveChangesAsync();
                }
                db.News.Attach(item);
                db.News.Remove(item);
                await db.SaveChangesAsync();
            }
            var clientType = db.ClientTypes.Where(n => n.IdChannel == channel.IdChannel).ToList();
            foreach (var med in clientType)
            {
                db.ClientTypes.Attach(med);
                db.ClientTypes.Remove(med);
                await db.SaveChangesAsync();
            }
            db.Channels.Attach(channel);
            db.Channels.Remove(channel);
            await db.SaveChangesAsync();
            return Redirect("~/Account/Account");
        }
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> addPost(List<string> data, List<string> Links)
        {
            News news = new News();
            news.RateCount = 0;
            news.Rate = 0;
            news.Date = DateTime.Now;
            news.IdChannel = Int32.Parse(data[0]);
            news.Header = data[1];
            news.Description = data[2];
            news.IdCategory = Int32.Parse(data[3]);
            db.News.Add(news);
            await db.SaveChangesAsync();
            if(Links != null) {
                foreach (var link in Links)
                {
                    if(link != null)
                    {
                        Media media = new Media();
                        media.Link = link;
                        media.IdNews = news.IdNews;
                        db.Media.Add(media);
                        await db.SaveChangesAsync();
                    }
                }
            }
            return Redirect("~/Account/Account");
        }
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> deleteProfile()
        {
            Client client = await db.Clients.FindAsync(ClientData.ID);
            if(client != null)
            {
                db.Clients.Remove(client);
                await db.SaveChangesAsync();
                return Redirect("~/Home/Index");
            }
            return BadRequest("Ошибка удаления аккаунта");
        }
    }
}
