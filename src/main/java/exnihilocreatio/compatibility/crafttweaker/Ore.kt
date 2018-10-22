package exnihilocreatio.compatibility.crafttweaker

import crafttweaker.CraftTweakerAPI
import crafttweaker.annotations.ZenRegister
import crafttweaker.api.item.IItemStack
import exnihilocreatio.registries.manager.ExNihiloRegistryManager
import exnihilocreatio.texturing.Color
import exnihilocreatio.util.ItemInfo
import net.minecraft.item.ItemStack
import stanhebben.zenscript.annotations.Optional
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod

/**
 * This one is slightly different, it has to be run in the preinit loader to work.
 */
@ZenClass("mods.exnihilocreatio.Ore")
@ZenRegister
object Ore {

}
