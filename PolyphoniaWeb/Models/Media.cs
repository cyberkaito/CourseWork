using System;
using System.Collections.Generic;

namespace PolyphoniaWeb.Models;

public partial class Media
{
    public int IdMedia { get; set; }

    public string Link { get; set; } = null!;

    public int IdNews { get; set; }

    public virtual News IdNewsNavigation { get; set; }
}
