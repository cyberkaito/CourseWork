using System;
using System.Collections.Generic;

namespace PolyphoniaWeb.Models;

public partial class Channel
{
    public int IdChannel { get; set; }

    public string Name { get; set; } = null!;

    public string Description { get; set; } = null!;

    public string? Avatar { get; set; }

    public double? Rate { get; set; }

    public double? RateCount { get; set; }

    public virtual ICollection<ClientType> ClientTypes { get; } = new List<ClientType>();

    public virtual ICollection<News> News { get; } = new List<News>();
}
