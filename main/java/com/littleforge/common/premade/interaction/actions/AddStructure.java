package com.littleforge.common.premade.interaction.actions;

import com.creativemd.creativecore.common.utils.math.Rotation;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.relative.StructureRelative;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.math.vec.LittleVecContext;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.place.Placement;
import com.creativemd.littletiles.common.util.place.PlacementMode;
import com.creativemd.littletiles.common.util.place.PlacementPreview;
import com.creativemd.littletiles.common.util.place.PlacementResult;
import com.littleforge.common.recipe.LittleForgeRecipes;
import com.littleforge.common.strucutres.type.premade.interactive.InteractivePremade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;

public abstract class AddStructure {
    
    private static String premadeID;
    
    public static String getPremadeID() {
        return premadeID;
    }
    
    /** @param premadeID
     *            This value needs to be set for AddStucture to work
     *            premadeID is the ID of premade you wish to add to
     *            the existing premade structure. */
    public static void setPremadeID(String premadeID) {
        AddStructure.premadeID = premadeID;
    }
    
    private static LittlePreviews adjustPreviews(InteractivePremade premade, LittlePreviews previews) {
        int x = previews.getSize().x;
        int y = previews.getSize().y;
        int z = previews.getSize().z;
        //(box.maxX * 2 - box.minX * 2) / 2, (box.maxY * 2 - box.minY * 2) / 2, (box.maxZ * 2 - box.minZ * 2) / 2
        LittleBox surrounding = previews.getSurroundingBox();
        StructureRelative strctureRelative = new StructureRelative(previews.getSurroundingBox(), previews.getContext());
        LittleVec doubledCenter = strctureRelative.getDoubledCenterVec();
        
        if (surrounding.getSize(Axis.X) % 2 != 0 && surrounding.getSize(Axis.X) != 1) {
            doubledCenter.add(new LittleVec(0, 0, 1));
        }
        if (surrounding.getSize(Axis.Y) % 2 != 0 && surrounding.getSize(Axis.Y) != 1) {
            doubledCenter.add(new LittleVec(1, 0, 0));
        }
        if (surrounding.getSize(Axis.Z) % 2 != 0 && surrounding.getSize(Axis.Z) != 1) {
            doubledCenter.add(new LittleVec(1, 0, 0));
        }
        
        LittlePreviews premadePreviews = LittleStructurePremade.getPreviews(premade.type.id);
        LittleVec premadePreviewSize = premadePreviews.getSize().copy();
        LittleVec previewsSize = previews.getSize();
        switch (premade.direction) {
        case NORTH:
            previews.flipPreviews(Axis.Z, doubledCenter);
            
            premadePreviewSize.setY(0);
            premadePreviewSize.setX(0);
            previews.movePreviews(premadePreviews.getContext(), premadePreviewSize);
            break;
        case EAST:
            if (previewsSize.x != previewsSize.z) {
                int offsetX = Math.abs(previewsSize.x - premadePreviewSize.z);
                offsetX /= 2;
                
                if (previews.getContext().size == 32) {
                    offsetX /= 2;
                }
                
                previews.movePreviews(premadePreviews.getContext(), new LittleVec(offsetX, 0, -offsetX));
                strctureRelative = new StructureRelative(previews.getSurroundingBox(), previews.getContext());
                previews.rotatePreviews(Rotation.Y_COUNTER_CLOCKWISE, strctureRelative.getDoubledCenterVec());
                previews.flipPreviews(Axis.X, strctureRelative.getDoubledCenterVec());
                
            } else {
                previews.rotatePreviews(Rotation.Y_COUNTER_CLOCKWISE, doubledCenter);
                previews.flipPreviews(Axis.X, doubledCenter);
            }
            break;
        case SOUTH:
            
            premadePreviewSize.setY(0);
            premadePreviewSize.setZ(0);
            previews.movePreviews(premadePreviews.getContext(), premadePreviewSize);
            break;
        
        case WEST:
            if (previewsSize.x != previewsSize.z) {
                
                int offsetX = (Math.abs(previewsSize.x - premadePreviewSize.z)) / 2;
                if (previews.getContext().size == 32)
                    offsetX /= 2;
                previews.movePreviews(premadePreviews.getContext(), new LittleVec(offsetX, 0, -offsetX));
                strctureRelative = new StructureRelative(previews.getSurroundingBox(), previews.getContext());
                previews.rotatePreviews(Rotation.Y_COUNTER_CLOCKWISE, strctureRelative.getDoubledCenterVec());
                
            } else {
                int offsetX = Math.abs(previewsSize.z - premadePreviewSize.z);
                int offsetZ = Math.abs(previewsSize.x - premadePreviewSize.x);
                previews.movePreviews(premadePreviews.getContext(), new LittleVec(offsetX, 0, offsetZ));
            }
            break;
        default:
            break;
        }
        return previews;
    }
    
    public static void toPremade(InteractivePremade premade, EntityPlayer player) {
        System.out.println(premade.direction);
        if (LittleForgeRecipes.takeIngredients(player, premade.type.id, premade.getSeriesMaxium(), premade.getSeriesAt())) {
            try {
                long minX = premade.getSurroundingBox().getMinX();
                long minY = premade.getSurroundingBox().getMinY();
                long minZ = premade.getSurroundingBox().getMinZ();
                
                LittleGridContext context = premade.getSurroundingBox().getContext();
                BlockPos min = new BlockPos(context.toBlockOffset(minX), context.toBlockOffset(minY), context.toBlockOffset(minZ));
                LittleVecContext minVec = new LittleVecContext(new LittleVec((int) (minX - (long) min.getX() * (long) context.size), (int) (minY - (long) min
                    .getY() * (long) context.size), (int) (minZ - (long) min.getZ() * (long) context.size)), context);
                
                LittlePreviews previews = LittleStructurePremade.getStructurePremadeEntry(premadeID).previews.copy(); // Change this line to support different states
                
                LittleVec previewMinVec = previews.getMinVec();
                LittlePreview preview = null;
                
                LittleVec previewSize = previews.getSize().copy();
                
                int editX = premade.getEditArea().minX;
                int editY = premade.getEditArea().minY;
                int editZ = premade.getEditArea().minZ;
                
                previews = adjustPreviews(premade, previews);
                
                minVec.forceContext(previews);
                for (LittlePreview prev : previews) {
                    prev.box.sub(previewMinVec);
                    prev.box.add(minVec.getVec());
                    
                    switch (premade.direction) {
                    case NORTH:
                        prev.box.sub(0, 0, editZ);
                        prev.box.add(editX, editY, 0);
                        prev.box.sub(0, 0, previewSize.z);
                        break;
                    case EAST:
                        prev.box.add(editZ, editY, editX);
                        break;
                    case SOUTH:
                        prev.box.sub(editX, 0, 0);
                        prev.box.add(0, editY, editZ);
                        prev.box.sub(previewSize.x, 0, 0);
                        break;
                    case WEST:
                        prev.box.add(0, editY, 0);
                        prev.box.sub(editZ, 0, editX);
                        //prev.box.sub(previewSize.z, 0, previewSize.z);
                        break;
                    default:
                        break;
                    }
                    preview = prev;
                }
                previews.convertToSmallest();
                
                /*
                if (removeStructure) {
                	premade.removeStructure();
                }
                */
                PlacementPreview nextPremade = new PlacementPreview(premade.getWorld(), previews, PlacementMode.all, preview.box, false, min, LittleVec.ZERO, EnumFacing.NORTH);
                Placement place = new Placement(null, nextPremade);
                PlacementResult result = place.tryPlace();
                if (result != null)
                    premade.linkStructure(result.parentStructure, premade.direction);
                else
                    player.sendStatusMessage(new TextComponentTranslation("structure.interaction.structurecollision").appendText(" " + place.pos.toString()), true);
                
            } catch (CorruptedConnectionException | NotYetConnectedException e1) {
                e1.printStackTrace();
            }
        }
    }
}
