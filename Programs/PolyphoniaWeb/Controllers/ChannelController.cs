using Microsoft.AspNetCore.Mvc;
using PolyphoniaWeb.Models;

namespace PolyphoniaWeb.Controllers
{
    public class ChannelController : Controller
    {
        private PolyphoniaDatabaseContext db;
        public ChannelController(PolyphoniaDatabaseContext context)
        {
            db = context;
        }
        public IActionResult Channel()
        {
            int ID = Convert.ToInt32(Request.Query["ID"]);
            Channel currentChannel = db.Channels.Find(ID);
            var clients = db.Clients.ToList();
            var clientTypes = db.ClientTypes.ToList();
            var media = db.Media.ToList();
            var news = db.News.ToList();
            var categories = db.Categories.ToList();
            var model = new ChannelModel(currentChannel, clients, clientTypes, categories, media, news );
            return View(model);
        }
    }
}
