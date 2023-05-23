using System.ComponentModel.DataAnnotations;

namespace PolyphoniaWeb.Models
{
    public class ClientData
    {
        public static int ID { get; set; }
        public static string? Name { get; set; }

        [Required(ErrorMessage = "Не указана почта")]
        public static string Email { get; set; }

        [Required(ErrorMessage = "Не указан пароль")]
        [DataType(DataType.Password)]
        public static string Key { get; set; }

        public static string? Avatar { get; set; }

        public static string? Bio { get; set; }
        public ClientData(string email, string key, int iD, string name, string avatar, string bio)
        {
            Email = email;
            Key = key;
            ID = iD;
            Name = name;
            Avatar = avatar;
            Bio = bio;
        }
    }
}
