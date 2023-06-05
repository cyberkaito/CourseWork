using Microsoft.AspNetCore.Mvc;
using PolyphoniaWeb.Models;
using System.Diagnostics;

namespace PolyphoniaWeb.Controllers
{
    public class HomeController : Controller
    {
        private PolyphoniaDatabaseContext db;

        public HomeController(PolyphoniaDatabaseContext context)
        {
            db = context;
        }
        public IActionResult Channels()
        {
            var channels = db.Channels.ToList();
            var clients = db.Clients.ToList();
            var clientTypes = db.ClientTypes.ToList();
            var roles = db.Roles.ToList();
            var model = new ChannelsModel(channels, clients, clientTypes,roles);
            return View(model);
        }
        public IActionResult Index()
        {
            var news = db.News.ToList();
            var categories = db.Categories.ToList();
            var media = db.Media.ToList();
            var clientTypes = db.ClientTypes.ToList();
            var clients = db.Clients.ToList();
            var model = new NewsMediaCategory(news, categories, media, clientTypes, clients);
            return View(model);
        }
        public IActionResult Article()
        {
            int ID = Convert.ToInt32(Request.Query["ID"]);
            News article = db.News.Find(ID);
            List<Media> media = db.Media.Where(n => n.IdNews == article.IdNews).ToList();
            List<Comment> comment = db.Comments.Where(n => n.IdNews == article.IdNews).ToList();
            ArticleNews articleNews = new ArticleNews(article, db.News.ToList(), db.Channels.ToList(),media, comment, db.Clients.ToList());
            return View(articleNews);
        }
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> addComment(List<string> data)
        {
            News news = db.News.FirstOrDefault(n => n.IdNews == Int32.Parse(data[0]));
            Client client = db.Clients.FirstOrDefault(n=> n.Email == HttpContext.Session.GetString("AuthUser"));
            Comment comment = new Comment();
            comment.IdClient = client.IdClient;
            comment.IdNews = news.IdNews;
            comment.Text = data[1];
            db.Comments.Add(comment);
            await db.SaveChangesAsync();
            return Redirect("~/Home/Article?ID="+news.IdNews);
        }
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> addRate(List<string> data)
        {
            News news = db.News.FirstOrDefault(n => n.IdNews == Int32.Parse(data[0]));
            news.RateCount += 1;
            news.Rate += Int32.Parse(data[1]);
            db.News.Attach(news);
            db.News.Update(news);
            await db.SaveChangesAsync();
            return Redirect("~/Home/Article?ID=" + news.IdNews);
        }
        public IActionResult Privacy()
        {
            return View();
        }

        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
        }
    }
}