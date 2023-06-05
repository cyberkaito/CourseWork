using System;
using System.Collections.Generic;

namespace PolyphoniaWeb.Models;

public partial class News
{
    public int? IdNews { get; set; }

    public string? Header { get; set; } = null!;

    public string? Description { get; set; } = null!;

    public double? Rate { get; set; }

    public double? RateCount { get; set; }

    public DateTime? Date { get; set; }

    public int? IdCategory { get; set; }

    public int? IdChannel { get; set; }
}
