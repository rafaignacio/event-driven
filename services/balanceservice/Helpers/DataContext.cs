using balanceservice.Models;
using Microsoft.EntityFrameworkCore;

namespace balanceservice.Helpers
{
    public class DataContext : DbContext
    {
        public DbSet<AccountViewModel> Accounts { get; set; }

        // public DataContext(DbContextOptions<DataContext> options) : base(options)
        // {
        // }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder.UseMySql("Server=userdb;port=3306;Database=BalanceDB;User=root;Password=test");
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<AccountViewModel>(entity =>
            {
                entity.HasKey(ent => ent.ID);
                entity.Property(ent => ent.CurrentBalance).HasDefaultValue(0).IsRequired();
                entity.Property(e => e.Host).IsRequired();
                entity.Property(e => e.UserID).IsRequired();
            });
        }
    }
}