package WayofTime.alchemicalWizardry.common.tileEntity.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;
import WayofTime.alchemicalWizardry.common.tileEntity.TEWritingTable;

public class ContainerWritingTable extends Container
{
    protected TEWritingTable tileEntity;

    public ContainerWritingTable(InventoryPlayer inventoryPlayer, TEWritingTable te)
    {
        tileEntity = te;
        //the Slot constructor takes the IInventory and the slot number in that it binds to
        //and the x-y coordinates it resides on-screen
        addSlotToContainer(new Slot(tileEntity, 0, 152, 110));
        addSlotToContainer(new Slot(tileEntity, 1, 80, 18));
        addSlotToContainer(new Slot(tileEntity, 2, 33, 52));
        addSlotToContainer(new Slot(tileEntity, 3, 51, 110));
        addSlotToContainer(new Slot(tileEntity, 4, 109, 110));
        addSlotToContainer(new Slot(tileEntity, 5, 127, 52));
        addSlotToContainer(new Slot(tileEntity, 6, 80, 67));
        //commonly used vanilla code that adds the player's inventory
        bindPlayerInventory(inventoryPlayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return tileEntity.isUseableByPlayer(player);
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 198));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
        ItemStack stack = null;
        Slot slotObject = (Slot) inventorySlots.get(slot);

        //null checks and checks if the item can be stacked (maxStackSize > 1)
        if (slotObject != null && slotObject.getHasStack())
        {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();

            //merges the item into player inventory since its in the tileEntity
            if (slot <= 6)
            {
                if (!this.mergeItemStack(stackInSlot, 7, 43, true))
                {
                    return null;
                }
            }
            else if(stack.getItem() instanceof IBloodOrb)
            {
            	if (!this.mergeItemStack(stackInSlot, 0, 1, false))
                {
                    return null;
                }
            }
            //places it into the tileEntity is possible since its in the player inventory
            else if (!this.mergeItemStack(stackInSlot, 1, 6, false))
            {
                return null;
            }

            if (stackInSlot.stackSize == 0)
            {
                slotObject.putStack(null);
            } else
            {
                slotObject.onSlotChanged();
            }

            if (stackInSlot.stackSize == stack.stackSize)
            {
                return null;
            }

            slotObject.onPickupFromSlot(player, stackInSlot);
        }

        return stack;
    }
}