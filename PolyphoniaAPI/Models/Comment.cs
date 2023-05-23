﻿using System;
using System.Collections.Generic;

namespace PolyphoniaWeb.Models;

public partial class Comment
{
    public int? IdComment { get; set; }

    public int? IdClient { get; set; }

    public int? IdNews { get; set; }

    public string? Text { get; set; } = null!;

}
