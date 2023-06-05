namespace PolyphoniaWeb.Models
{
    public class AdminModel
    {
        public List<Channel> Channels { get; set; }
        public List<Client> Clients { get; set; }
        public List<ClientType> ClientTypes { get; set; }
        public List<Role> Roles { get; set; }
        public List<Category> Categories { get; set; }
        public List<News> News { get; set; }
        public List<Media> Media { get; set; }
        public List<Comment> Comments { get; set; }
        public AdminModel(List<Channel> channels, List<Client> clients, List<ClientType> clientTypes, List<Role> roles, 
            List<Category> categories, List<News> news, List<Media> media, List<Comment> comments)
        {
            Channels = channels;
            Clients = clients;
            Roles = roles;
            Categories = categories;
            News = news;
            Media = media;
            ClientTypes = clientTypes;
            Comments = comments;
        }
    }
}
