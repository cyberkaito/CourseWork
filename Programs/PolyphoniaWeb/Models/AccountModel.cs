namespace PolyphoniaWeb.Models
{
    public class AccountModel
    {
        public List<Channel> Channels { get; set; }
        public List<ClientType> ClientTypes { get; set; }
        public List<News> News { get; set; }
        public List<Media> Media { get; set; }
        public List<Comment> Comments { get; set; }
        public ClientData ClientData { get; set; }
        public List<Category> Categories { get; set; }
        public AccountModel(List<Channel> channels, List<ClientType> clientTypes, List<News> news, List<Media> media, List<Comment> comments, List<Category> categories, ClientData clientData)
        {
            Channels = channels;
            News = news;
            Media = media;
            ClientTypes = clientTypes;
            Comments = comments;
            ClientData = clientData;
            Categories = categories;
        }
    }
}
