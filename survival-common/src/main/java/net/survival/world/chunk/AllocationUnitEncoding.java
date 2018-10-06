package net.survival.world.chunk;

// TODO: Rename to VauEncoding.
//       VAU = Virtual Allocation Unit
class AllocationUnitEncoding
{
    //
    // Bits 0..30:  address
    // Bit  31:     allocated flag
    // Bits 32..62: length
    // Bit  63:     invalid flag
    //


    private static final long INVALID_FLAG   = 0x8000000000000000L;
    private static final long LENGTH_MASK    = 0x7FFFFFFF00000000L;
    private static final long LENGTH_SHIFT   = 32L;
    private static final long ALLOCATED_FLAG = 0x0000000080000000L;
    private static final long ADDRESS_MASK   = 0x000000007FFFFFFFL;
    private static final long UNIT_LENGTH    = 32;

    // UPPER_BOUND = 2^(# of address bits) + 2^(# of length bits)
    //             = 2^55 + 2^16
    public static final long LOWER_BOUND = 0L;
    public static final long UPPER_BOUND = UNIT_LENGTH * ((1L << 31L) - 1);

    public static final long INVALID_EAU    = INVALID_FLAG;

    public static long alignAddress(long address) {
        return (address / UNIT_LENGTH) * UNIT_LENGTH;
    }

    public static long padLength(long length) {
        return ((length - 1L) / UNIT_LENGTH + 1L) * UNIT_LENGTH;
    }

    public static long encode(long alignedAddress, long paddedLength, boolean allocated) {
        if (alignedAddress % UNIT_LENGTH != 0)
            throw new IllegalArgumentException("Precondition is not met: alignedAddress must be divisible by UNIT_LENGTH.");
        if (paddedLength % UNIT_LENGTH != 0)
            throw new IllegalArgumentException("Precondition is not met: paddedLength must be divisible by UNIT_LENGTH.");

        return encodeUnchecked(alignedAddress, paddedLength, allocated);
    }

    public static long alignAndEncode(long address, long length, boolean allocated) {
        return encodeUnchecked(alignAddress(address), padLength(length), allocated);
    }

    private static long encodeUnchecked(long alignedAddress, long paddedLength, boolean allocated) {
        long encodedAddress = (alignedAddress / UNIT_LENGTH) & ADDRESS_MASK;
        long encodedLength = ((paddedLength / UNIT_LENGTH) << LENGTH_SHIFT) & LENGTH_MASK;
        long encodedAllocatedFlag = (allocated ? ALLOCATED_FLAG : 0L);
        return encodedAddress | encodedLength | encodedAllocatedFlag;
    }

    public static long decodeAddress(long encodedAllocationUnit) {
        if (encodedAllocationUnit == INVALID_EAU)
            return -1L;

        return (encodedAllocationUnit & ADDRESS_MASK) * UNIT_LENGTH;
    }

    public static long decodeLength(long encodedAllocationUnit) {
        if (encodedAllocationUnit == INVALID_EAU)
            return -1L;

        return ((encodedAllocationUnit & LENGTH_MASK) >>> LENGTH_SHIFT) * UNIT_LENGTH;
    }

    public static boolean decodeAllocatedFlag(long encodedAllocationUnit) {
        if (encodedAllocationUnit == INVALID_EAU)
            return false;

        return (encodedAllocationUnit & ALLOCATED_FLAG) != 0L;
    }
}