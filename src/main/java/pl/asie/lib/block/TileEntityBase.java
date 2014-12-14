package pl.asie.lib.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBase extends TileEntity {
	// Base functions for containers
	public void openInventory() {
		
	}
	public void closeInventory() {
		
	}
	public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this
			&& player.getDistanceSq(
			(double) this.xCoord + 0.5D,
			(double) this.yCoord + 0.5D,
			(double) this.zCoord + 0.5D) <= 64.0D;
	}
	
	// Remote NBT data management
	public void readFromRemoteNBT(NBTTagCompound tag) { }
	public void writeToRemoteNBT(NBTTagCompound tag) { }
	
	@Override
	public net.minecraft.network.Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToRemoteNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound tag = pkt.func_148857_g();
		if(tag != null)
			this.readFromRemoteNBT(tag);
	}
	
	// Dummy functions
	@Deprecated
	public void onWorldUnload() { }
	public void onBlockDestroy() { }
	
	// Redstone management (TODO: Move to TileMachine)
	public void onRedstoneSignal(int signal) {
		
	}
	
	private int oldRedstoneSignal = -1000;
	
	protected void onRedstoneSignal_internal(int redstoneSignal) {
		if(redstoneSignal == oldRedstoneSignal) return;
		oldRedstoneSignal = redstoneSignal + 1000;
		this.onRedstoneSignal(redstoneSignal);
	}

	public int requestCurrentRedstoneValue(int side) {
		return 0;
	}
}
