package pl.asie.lib.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

public abstract class ContainerBase extends ContainerInventory {

	private final TileEntityBase entity;

	public ContainerBase(TileEntityBase entity, InventoryPlayer inventoryPlayer) {
		super(entity instanceof IInventory ? ((IInventory) entity) : null);
		this.entity = entity;

		entity.openInventory();
	}

	public TileEntityBase getEntity() {
		return entity;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.entity.isUseableByPlayer(player);
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		final InventoryPlayer inventoryPlayer = player.inventory;
		if(inventoryPlayer.getItemStack() != null) {
			player.dropPlayerItemWithRandomChoice(inventoryPlayer.getItemStack(), false);
			inventoryPlayer.setItemStack(null);
		}
		this.entity.closeInventory();
		if(this.entity instanceof TileEntityInventory && ((TileEntityInventory) this.entity).getInventoryName() != null && ((TileEntityInventory) this.entity).getInventoryName().equals("Showcase")) {
			try {
				((TileEntityInventory) this.entity).getClass().getField("numUsingPlayers").setInt(((TileEntityInventory) this.entity), 0);
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {}
			
			this.entity.getWorldObj().markBlockForUpdate(entity.xCoord, entity.yCoord, entity.zCoord);
		}
	}
}
