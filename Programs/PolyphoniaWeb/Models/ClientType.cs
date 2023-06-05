using System;
using System.Collections.Generic;

namespace PolyphoniaWeb.Models;

public partial class ClientType
{
    public int IdClientType { get; set; }

    public int IdClient { get; set; }

    public int IdChannel { get; set; }

    public int IdRole { get; set; }

    public virtual Channel IdChannelNavigation { get; set; } = null!;

    public virtual Client IdClientNavigation { get; set; } = null!;

    public virtual Role IdRoleNavigation { get; set; } = null!;
}
