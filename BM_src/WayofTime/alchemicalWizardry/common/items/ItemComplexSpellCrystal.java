package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyRecipeRegistry;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellParadigmBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.List;

import org.lwjgl.input.Keyboard;

public class ItemComplexSpellCrystal extends EnergyItems
{
    public ItemComplexSpellCrystal(int par1)
    {
        super(par1);
        this.setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:ComplexCrystal");
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Crystal of unimaginable power");

        if (!(par1ItemStack.stackTagCompound == null))
        {
            NBTTagCompound itemTag = par1ItemStack.stackTagCompound;

            if (!par1ItemStack.stackTagCompound.getString("ownerName").equals(""))
            {
                par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
            }

            par3List.add("Coords: " + itemTag.getInteger("xCoord") + ", " + itemTag.getInteger("yCoord") + ", " + itemTag.getInteger("zCoord"));
            par3List.add("Bound Dimension: " + getDimensionID(par1ItemStack));
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
        {
            ItemStack[] recipe = AlchemyRecipeRegistry.getRecipeForItemStack(par1ItemStack);

            if (recipe != null)
            {
                par3List.add(EnumChatFormatting.BLUE + "Recipe:");

                for (ItemStack item : recipe)
                {
                    if (item != null)
                    {
                        par3List.add("" + item.getDisplayName());
                    }
                }
            }
        } else
        {
        	ItemStack[] recipe = AlchemyRecipeRegistry.getRecipeForItemStack(par1ItemStack);
        	if(recipe!=null)
        	{
        		par3List.add("-Press " + EnumChatFormatting.BLUE + "shift" + EnumChatFormatting.GRAY + " for Recipe-");
        	}	
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (!par2World.isRemote)
        {
            //World world = MinecraftServer.getServer().worldServers[getDimensionID(par1ItemStack)];
            World world = DimensionManager.getWorld(getDimensionID(par1ItemStack));

            if (world != null)
            {
                NBTTagCompound itemTag = par1ItemStack.stackTagCompound;
                TileEntity tileEntity = world.getBlockTileEntity(itemTag.getInteger("xCoord"), itemTag.getInteger("yCoord"), itemTag.getInteger("zCoord"));

                if (tileEntity instanceof TESpellParadigmBlock)
                {
                    TESpellParadigmBlock tileParad = (TESpellParadigmBlock) tileEntity;

                    tileParad.castSpell(par2World, par3EntityPlayer, par1ItemStack);
                } else
                {
                    return par1ItemStack;
                }
            } else
            {
                return par1ItemStack;
            }
        } else
        {
            return par1ItemStack;
        }

        par2World.playSoundAtEntity(par3EntityPlayer, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
//        if (!par2World.isRemote)
//        {
//            //par2World.spawnEntityInWorld(new EnergyBlastProjectile(par2World, par3EntityPlayer, damage));
//            par2World.spawnEntityInWorld(new FireProjectile(par2World, par3EntityPlayer, 10));
//        }
        return par1ItemStack;
    }

    public int getDimensionID(ItemStack itemStack)
    {
        if (itemStack.stackTagCompound == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        return itemStack.stackTagCompound.getInteger("dimensionId");
    }
}
