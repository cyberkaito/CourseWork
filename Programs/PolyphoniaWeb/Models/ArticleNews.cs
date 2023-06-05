namespace PolyphoniaWeb.Models
{
    public class ArticleNews
    {
        public News news { get; set; }
        public List<News> news_list { get; set; }
        public List<Channel> channel_list { get; set; }
        public List<Media> Media_list { get; set; }
        public List<Comment> Comments { get; set; }
        public List<Client> Clients { get; set; }
        public ArticleNews(News _news, List<News> _news_list, List<Channel> channel_list, List<Media> media_list, List<Comment> comments, List<Client> clients)
        {
            news = _news;
            news_list = _news_list;
            this.channel_list = channel_list;
            Media_list = media_list;
            Comments = comments;
            Clients = clients;
        }
    }
}
