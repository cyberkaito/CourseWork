namespace PolyphoniaWeb.Models
{
    public class ChannelsModel
    {
        public List<Channel> Channels { get; set; }
        public List<Client> Clients { get; set; }
        public List<ClientType> ClientTypes { get; set; }
        public List<Role> Roles { get; set; }
        public ChannelsModel(List<Channel> channels, List<Client> clients, List<ClientType> clientTypes, List<Role> roles)
        {
            Channels = channels;
            Clients = clients;
            ClientTypes = clientTypes;
            Roles = roles;
        }
    }
}
