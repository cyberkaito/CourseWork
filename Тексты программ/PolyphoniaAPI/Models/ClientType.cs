using System;
using System.Collections.Generic;

namespace PolyphoniaWeb.Models;

public partial class ClientType
{
    public int? IdClientType { get; set; }

    public int? IdClient { get; set; }

    public int? IdChannel { get; set; }

    public int? IdRole { get; set; }
}
