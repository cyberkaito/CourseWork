using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;

namespace PolyphoniaWeb.Models;

public partial class PolyphoniaDatabaseContext : DbContext
{
    public PolyphoniaDatabaseContext()
    {
    }

    public PolyphoniaDatabaseContext(DbContextOptions<PolyphoniaDatabaseContext> options)
        : base(options)
    {
    }

    public virtual DbSet<Category> Categories { get; set; }

    public virtual DbSet<Channel> Channels { get; set; }

    public virtual DbSet<Client> Clients { get; set; }

    public virtual DbSet<ClientType> ClientTypes { get; set; }

    public virtual DbSet<Comment> Comments { get; set; }

    public virtual DbSet<Media> Media { get; set; }

    public virtual DbSet<News> News { get; set; }

    public virtual DbSet<Role> Roles { get; set; }
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Category>(entity =>
        {
            entity.HasKey(e => e.IdCategory).HasName("PK_ID_Category");

            entity.ToTable("Category");

            entity.HasIndex(e => e.Name, "UQ_Name_Category").IsUnique();

            entity.Property(e => e.IdCategory).HasColumnName("ID_Category");
            entity.Property(e => e.Name)
                .HasMaxLength(21)
                .IsUnicode(false);
        });

        modelBuilder.Entity<Channel>(entity =>
        {
            entity.HasKey(e => e.IdChannel).HasName("PK_ID_Channel");

            entity.ToTable("Channel");

            entity.HasIndex(e => e.Name, "UQ_Name_Channel").IsUnique();

            entity.Property(e => e.IdChannel).HasColumnName("ID_Channel");
            entity.Property(e => e.Avatar).IsUnicode(false);
            entity.Property(e => e.Description).IsUnicode(false);
            entity.Property(e => e.Name)
                .HasMaxLength(25)
                .IsUnicode(false);
            entity.Property(e => e.RateCount).HasColumnName("Rate_Count");
        });

        modelBuilder.Entity<Client>(entity =>
        {
            entity.HasKey(e => e.IdClient).HasName("PK_ID_Client");

            entity.ToTable("Client");

            entity.HasIndex(e => e.Email, "UQ_Email").IsUnique();

            entity.HasIndex(e => e.Name, "UQ_Name").IsUnique();

            entity.Property(e => e.IdClient).HasColumnName("ID_Client");
            entity.Property(e => e.Avatar).IsUnicode(false);
            entity.Property(e => e.Bio)
                .HasMaxLength(45)
                .IsUnicode(false);
            entity.Property(e => e.Email)
                .HasMaxLength(30)
                .IsUnicode(false);
            entity.Property(e => e.Key)
                .HasMaxLength(20)
                .IsUnicode(false);
            entity.Property(e => e.Name)
                .HasMaxLength(12)
                .IsUnicode(false);
        });

        modelBuilder.Entity<ClientType>(entity =>
        {
            entity.HasKey(e => e.IdClientType).HasName("PK_ID_Client_Type");

            entity.ToTable("Client_Type");

            entity.Property(e => e.IdClientType).HasColumnName("ID_Client_Type");
            entity.Property(e => e.IdChannel).HasColumnName("ID_Channel");
            entity.Property(e => e.IdClient).HasColumnName("ID_Client");
            entity.Property(e => e.IdRole).HasColumnName("ID_Role");

            entity.HasOne(d => d.IdChannelNavigation).WithMany(p => p.ClientTypes)
                .HasForeignKey(d => d.IdChannel)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_ID_Channel_Client_Type");

            entity.HasOne(d => d.IdClientNavigation).WithMany(p => p.ClientTypes)
                .HasForeignKey(d => d.IdClient)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_ID_Client");

            entity.HasOne(d => d.IdRoleNavigation).WithMany(p => p.ClientTypes)
                .HasForeignKey(d => d.IdRole)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_ID_Role");
        });

        modelBuilder.Entity<Comment>(entity =>
        {
            entity.HasKey(e => e.IdComment).HasName("PK_ID_Comment");

            entity.ToTable("Comment");

            entity.Property(e => e.IdComment).HasColumnName("ID_Comment");
            entity.Property(e => e.IdClient).HasColumnName("ID_Client");
            entity.Property(e => e.IdNews).HasColumnName("ID_News");
            entity.Property(e => e.Text)
                .HasMaxLength(100)
                .IsUnicode(false);

            entity.HasOne(d => d.IdClientNavigation).WithMany(p => p.Comments)
                .HasForeignKey(d => d.IdClient)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_ID_Client_Comment");

            entity.HasOne(d => d.IdNewsNavigation).WithMany(p => p.Comments)
                .HasForeignKey(d => d.IdNews)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_ID_News_Comment");
        });

        modelBuilder.Entity<Media>(entity =>
        {
            entity.HasKey(e => e.IdMedia).HasName("PK_ID_Media");

            entity.Property(e => e.IdMedia).HasColumnName("ID_Media");
            entity.Property(e => e.IdNews).HasColumnName("ID_News");
            entity.Property(e => e.Link).IsUnicode(false);

            entity.HasOne(d => d.IdNewsNavigation).WithMany(p => p.Media)
                .HasForeignKey(d => d.IdNews)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_ID_News_Media");
        });

        modelBuilder.Entity<News>(entity =>
        {
            entity.HasKey(e => e.IdNews).HasName("PK_ID_News");

            entity.Property(e => e.IdNews).HasColumnName("ID_News");
            entity.Property(e => e.Date).HasColumnType("datetime");
            entity.Property(e => e.Description).IsUnicode(false);
            entity.Property(e => e.Header)
                .HasMaxLength(45)
                .IsUnicode(false);
            entity.Property(e => e.IdCategory).HasColumnName("ID_Category");
            entity.Property(e => e.IdChannel).HasColumnName("ID_Channel");
            entity.Property(e => e.RateCount).HasColumnName("Rate_Count");

            entity.HasOne(d => d.IdCategoryNavigation).WithMany(p => p.News)
                .HasForeignKey(d => d.IdCategory)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_ID_Category");

            entity.HasOne(d => d.IdChannelNavigation).WithMany(p => p.News)
                .HasForeignKey(d => d.IdChannel)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_ID_Channel");
        });

        modelBuilder.Entity<Role>(entity =>
        {
            entity.HasKey(e => e.IdRole).HasName("PK_ID_Role");

            entity.ToTable("Role");

            entity.HasIndex(e => e.Name, "UQ_Name_Role").IsUnique();

            entity.Property(e => e.IdRole).HasColumnName("ID_Role");
            entity.Property(e => e.Name)
                .HasMaxLength(25)
                .IsUnicode(false);
        });

        OnModelCreatingPartial(modelBuilder);
    }

    partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
}
