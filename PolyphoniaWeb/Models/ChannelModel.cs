namespace PolyphoniaWeb.Models
{
    public class ChannelModel
    {
        public Channel CurrentChannel { get; set; }
        public List<Client> Clients { get; set; }
        public List<ClientType> ClientTypes { get; set; }
        public List<Category> Categories { get; set; }
        public List<Media> Media { get; set; }
        public List<News> News { get; set; }
        public ChannelModel(Channel channels, List<Client> clients, List<ClientType> clientTypes, List<Category> categories, List<Media> media, List<News> news)
        {
            CurrentChannel = channels;
            Clients = clients;
            ClientTypes = clientTypes;
            Categories = categories;
            Media = media;
            News = news;
        }
    }
}
