using System;
using System.Collections.Generic;

namespace PolyphoniaWeb.Models;

public partial class Category
{
    public int IdCategory { get; set; }

    public string Name { get; set; } = null!;

    public virtual ICollection<News> News { get; } = new List<News>();

    public static implicit operator Category(List<Category> v)
    {
        throw new NotImplementedException();
    }
}
