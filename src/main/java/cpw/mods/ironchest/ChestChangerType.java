/*******************************************************************************
 * Copyright (c) 2012 cpw. All rights reserved. This program and the accompanying materials are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at http://www.gnu.org/licenses/gpl.html
 *
 * Contributors: cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest;

import static cpw.mods.ironchest.IronChestType.COPPER;
import static cpw.mods.ironchest.IronChestType.CRYSTAL;
import static cpw.mods.ironchest.IronChestType.DIAMOND;
import static cpw.mods.ironchest.IronChestType.GOLD;
import static cpw.mods.ironchest.IronChestType.IRON;
import static cpw.mods.ironchest.IronChestType.OBSIDIAN;
import static cpw.mods.ironchest.IronChestType.SILVER;
import static cpw.mods.ironchest.IronChestType.WOOD;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import cpw.mods.ironchest.client.ModelHelper;
import net.minecraftforge.fml.relauncher.Side;

public enum ChestChangerType {
    IRONGOLD(IRON, GOLD, "ironGoldUpgrade", "Iron to Gold Chest Upgrade", "mmm", "msm", "mmm"),
    GOLDDIAMOND(GOLD, DIAMOND, "goldDiamondUpgrade", "Gold to Diamond Chest Upgrade", "GGG", "msm", "GGG"),
    COPPERSILVER(COPPER, SILVER, "copperSilverUpgrade", "Copper to Silver Chest Upgrade", "mmm", "msm", "mmm"),
    SILVERGOLD(SILVER, GOLD, "silverGoldUpgrade", "Silver to Gold Chest Upgrade", "mGm", "GsG", "mGm"),
    COPPERIRON(COPPER, IRON, "copperIronUpgrade", "Copper to Iron Chest Upgrade", "mGm", "GsG", "mGm"),
    DIAMONDCRYSTAL(DIAMOND, CRYSTAL, "diamondCrystalUpgrade", "Diamond to Crystal Chest Upgrade", "GGG", "GOG", "GGG"),
    WOODIRON(WOOD, IRON, "woodIronUpgrade", "Normal chest to Iron Chest Upgrade", "mmm", "msm", "mmm"),
    WOODCOPPER(WOOD, COPPER, "woodCopperUpgrade", "Normal chest to Copper Chest Upgrade", "mmm", "msm", "mmm"),
    DIAMONDOBSIDIAN(DIAMOND, OBSIDIAN, "diamondObsidianUpgrade", "Diamond to Obsidian Chest Upgrade", "mmm", "mGm", "mmm");

    private IronChestType source;
    private IronChestType target;
    public String itemName;
    public String descriptiveName;
    public ItemChestChanger item;
    private String[] recipe;

    private ChestChangerType(IronChestType source, IronChestType target, String itemName, String descriptiveName, String... recipe)
    {
        this.source = source;
        this.target = target;
        this.itemName = itemName;
        this.descriptiveName = descriptiveName;
        this.recipe = recipe;
    }
    
    public IronChestType getSource(){
    	return source;
    }

    public boolean canUpgrade(IronChestType from)
    {
        return from == this.source;
    }

    public int getTarget()
    {
        return this.target.ordinal();
    }

    public ItemChestChanger buildItem()
    {
        item = new ItemChestChanger(this);
        GameRegistry.registerItem(item, itemName);
        if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            ModelHelper.registerItem(item, "ironchest:" + itemName);
        return item;
    }

    public void addRecipes()
    {
        for (String sourceMat : source.getMatList())
        {
            for (String targetMat : target.getMatList())
            {
                Object targetMaterial = IronChestType.translateOreName(targetMat);
                Object sourceMaterial = IronChestType.translateOreName(sourceMat);
                IronChestType.addRecipe(new ItemStack(item), recipe, 'm', targetMaterial, 's', sourceMaterial, 'G', "blockGlass", 'O', Blocks.obsidian);
            }
        }
    }

    public static void buildItems()
    {
        for (ChestChangerType type : values())
        {
            type.buildItem();
        }
    }

    public static void generateRecipes()
    {
        for (ChestChangerType item : values())
        {
            item.addRecipes();
        }
    }
}
