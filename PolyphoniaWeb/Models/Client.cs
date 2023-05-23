using System;
using System.Collections.Generic;

namespace PolyphoniaWeb.Models;

public partial class Client
{
    public int IdClient { get; set; }

    public string Name { get; set; } = null!;

    public string Email { get; set; } = null!;

    public string Key { get; set; } = null!;

    public string? Avatar { get; set; }

    public string? Bio { get; set; }

    public virtual ICollection<ClientType> ClientTypes { get; } = new List<ClientType>();

    public virtual ICollection<Comment> Comments { get; } = new List<Comment>();
}
