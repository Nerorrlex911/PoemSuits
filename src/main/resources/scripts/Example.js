const Player = find("org.bukkit.entity.Player");
const Damageable = find("org.bukkit.entity.Damageable");
const Particle = find("org.bukkit.Particle");
const Vector = find("org.bukkit.util.Vector");

function main() {
    const player = this.player
    player.sendMessage("套装件数(js)>>"+this.suit_amount)
    container().set(player.getUniqueId(),"suitSkill",this.suit_amount)
}
//@Listener(-event org.bukkit.event.entity.EntityDamageByEntityEvent)
function onDamaged(event) {
    const player = event.entity;
    if(player != null && player instanceof Player) {
        const suit_amount = container().get(player.getUniqueId(),"suitSkill")
        if(suit_amount*0.1 < Math.random()) {
            return;
        }
        player.spawnParticle(Particle.EXPLOSION_LARGE,player.getLocation(),3)
        const entities = player.getNearbyEntities(5,5,5)
        for (var i = entities.length - 1; i >= 0; i--) {
            const target = entities[i]
            print(target)
            if(target instanceof Damageable) {
                target.damage(15,player)
                const vector = target.getLocation().subtract(player.getLocation()).toVector().normalize().multiply(2).add(new Vector(0,1,0))
                target.setVelocity(vector)
            }
        }
    }
}