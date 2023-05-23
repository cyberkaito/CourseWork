using Microsoft.AspNetCore.Mvc;
using PolyphoniaWeb.Models;

namespace PolyphoniaWeb.Controllers
{
    public class AdminController : Controller
    {
        private PolyphoniaDatabaseContext db;
        public AdminController(PolyphoniaDatabaseContext context)
        {
            db = context;
        }
        public ActionResult Admin()
        {
            var channels = db.Channels.ToList();
            var clients = db.Clients.ToList();
            var clientTypes = db.ClientTypes.ToList();
            var roles = db.Roles.ToList();
            var categories = db.Categories.ToList();
            var news = db.News.ToList();
            var media = db.Media.ToList();
            var comment = db.Comments.ToList();
            var model = new AdminModel(channels, clients, clientTypes, roles, categories, news, media, comment);
            return View(model);
        }
        // POST TABLES
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> addChannel(List<string> data)
        {
            Channel channel = new Channel();
            channel.Name = data[1];
            channel.Description = data[2];
            channel.Avatar = data[3];
            channel.Rate = Int32.Parse(data[4]);
            channel.RateCount = Int32.Parse(data[5]);
            switch (data[data.Count - 1])
            {
                case "Добавить":
                    db.Channels.Add(channel);
                    break;
                case "Обновить":
                    channel.IdChannel = Int32.Parse(data[0]);
                    db.Channels.Attach(channel);
                    db.Channels.Update(channel);
                    break;
                case "Удалить":
                    channel.IdChannel = Int32.Parse(data[0]);
                    db.Channels.Attach(channel);
                    db.Channels.Remove(channel);
                    break;
            }
            await db.SaveChangesAsync();
            return Redirect("~/Admin/Admin");
        }
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> addCategory(List<string> data)
        {
            Category category = new Category();
            category.Name = data[1];
            switch (data[data.Count - 1])
            {
                case "Добавить":
                    db.Categories.Add(category);
                    break;
                case "Обновить":
                    category.IdCategory = Int32.Parse(data[0]);
                    db.Categories.Attach(category);
                    db.Categories.Update(category);
                    break;
                case "Удалить":
                    category.IdCategory = Int32.Parse(data[0]);
                    db.Categories.Attach(category);
                    db.Categories.Remove(category);
                    break;
            }
            await db.SaveChangesAsync();
            return Redirect("~/Admin/Admin");
        }
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> addClient(List<string> data)
        {
            Client client = new Client();
            client.Name = data[1];
            client.Email = data[2];
            client.Key = data[3];
            client.Avatar = data[4];
            client.Bio = data[5];
            switch (data[data.Count - 1])
            {
                case "Добавить":
                    db.Clients.Add(client);
                    break;
                case "Обновить":
                    client.IdClient = Int32.Parse(data[0]);
                    db.Clients.Attach(client);
                    db.Clients.Update(client);
                    break;
                case "Удалить":
                    client.IdClient = Int32.Parse(data[0]);
                    db.Clients.Attach(client);
                    db.Clients.Remove(client);
                    break;
            }
            await db.SaveChangesAsync();
            return Redirect("~/Admin/Admin");
        }
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> addClientType(List<string> data)
        {
            ClientType clientTypes = new ClientType();
            clientTypes.IdClient = Int32.Parse(data[1]);
            clientTypes.IdChannel = Int32.Parse(data[2]);
            clientTypes.IdRole = Int32.Parse(data[3]);
            switch (data[data.Count - 1])
            {
                case "Добавить":
                    db.ClientTypes.Add(clientTypes);
                    break;
                case "Обновить":
                    clientTypes.IdClientType = Int32.Parse(data[0]);
                    db.ClientTypes.Attach(clientTypes);
                    db.ClientTypes.Update(clientTypes);
                    break;
                case "Удалить":
                    clientTypes.IdClientType = Int32.Parse(data[0]);
                    db.ClientTypes.Attach(clientTypes);
                    db.ClientTypes.Remove(clientTypes);
                    break;
            }
            await db.SaveChangesAsync();
            return Redirect("~/Admin/Admin");
        }
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> addComments(List<string> data)
        {
            Comment comments = new Comment();
            comments.IdClient = Int32.Parse(data[1]);
            comments.IdNews = Int32.Parse(data[2]);
            comments.Text = data[3];
            switch (data[data.Count - 1])
            {
                case "Добавить":
                    db.Comments.Add(comments);
                    break;
                case "Обновить":
                    comments.IdComment = Int32.Parse(data[0]);
                    db.Comments.Attach(comments);
                    db.Comments.Update(comments);
                    break;
                case "Удалить":
                    comments.IdComment = Int32.Parse(data[0]);
                    db.Comments.Attach(comments);
                    db.Comments.Remove(comments);
                    break;
            }
            await db.SaveChangesAsync();
            return Redirect("~/Admin/Admin");
        }
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> addMedia(List<string> data)
        {
            Media media = new Media();
            media.Link = data[1];
            media.IdNews = Int32.Parse(data[2]);
            switch (data[data.Count - 1])
            {
                case "Добавить":
                    db.Media.Add(media);
                    break;
                case "Обновить":
                    media.IdMedia = Int32.Parse(data[0]);
                    db.Media.Attach(media);
                    db.Media.Update(media);
                    break;
                case "Удалить":
                    media.IdMedia = Int32.Parse(data[0]);
                    db.Media.Attach(media);
                    db.Media.Remove(media);
                    break;
            }
            await db.SaveChangesAsync();
            return Redirect("~/Admin/Admin");
        }
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> addNews(List<string> data)
        {
            News news = new News();
            news.Header = data[1];
            news.Description = data[2];
            news.Rate = Double.Parse(data[3]);
            news.RateCount = Double.Parse(data[4]);
            news.Date = DateTime.Parse(data[5]);
            news.IdCategory = Int32.Parse(data[6]);
            news.IdChannel = Int32.Parse(data[7]);
            switch (data[data.Count - 1])
            {
                case "Добавить":
                    db.News.Add(news);
                    break;
                case "Обновить":
                    news.IdNews = Int32.Parse(data[0]);
                    db.News.Attach(news);
                    db.News.Update(news);
                    break;
                case "Удалить":
                    news.IdNews = Int32.Parse(data[0]);
                    db.News.Attach(news);
                    db.News.Remove(news);
                    break;
            }
            await db.SaveChangesAsync();
            return Redirect("~/Admin/Admin");
        }
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> addRole(List<string> data)
        {
            Role role = new Role();
            role.Name = data[1];
            switch (data[data.Count - 1])
            {
                case "Добавить":
                    db.Roles.Add(role);
                    break;
                case "Обновить":
                    role.IdRole = Int32.Parse(data[0]);
                    db.Roles.Attach(role);
                    db.Roles.Update(role);
                    break;
                case "Удалить":
                    role.IdRole = Int32.Parse(data[0]);
                    db.Roles.Attach(role);
                    db.Roles.Remove(role);
                    break;
            }
            await db.SaveChangesAsync();
            return Redirect("~/Admin/Admin");
        }
    }
}
