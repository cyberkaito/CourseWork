namespace PolyphoniaWeb.Models
{
    public class NewsMediaCategory
    {
        public List<Category> Category { get; set; }
        public List<News> News { get; set; }
        public List<Media> Media { get; set; } 
        public List<ClientType> ClientTypes { get; set; }
        public List<Client> Clients { get; set; }
        public NewsMediaCategory(List<News> news, List<Category> category, List<Media> media, List<ClientType> clientTypes, List<Client> clients)
        {
            Category = category;
            News = news;   
            Media = media;
            ClientTypes = clientTypes;
            Clients = clients;
        }
    }
}
