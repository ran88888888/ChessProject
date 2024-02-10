package main;

public class BitBoard {
    public long bitboard;

    public BitBoard()
    {
        bitboard = 0;
    }

    public BitBoard(long number)
    {
        bitboard = number;
    }

    public BitBoard(String bits)
    {
        if (bits.length() > 64)
        {
            bits = bits.substring(0, 64);
        }
        try
        {
            bitboard = Long.parseLong(bits, 2);
        } catch (NumberFormatException e)
        {
            bitboard = 0;
        }
    }

    public int bitSetCount()
    {
        return Long.bitCount(bitboard);
    }

    public void clearBit(int position)
    {
        long temp = 1;
        temp = ~(temp << position);
        bitboard = bitboard & temp;
    }

    public void toggleBit(int position)
    {
        long temp = 1;
        temp = temp << position;
        bitboard = bitboard ^ temp;
    }

    public boolean isBitSet(int position)
    {
        long temp = 1;
        temp = temp << position;
        return (bitboard & temp) != 0;
    }

    public String toBinaryString()
    {
        return Long.toBinaryString(bitboard);
    }

    public long getRank(int rankNumber)
    {
        long temp = Long.parseUnsignedLong("255");
        temp = temp << (8 * (rankNumber - 1));
        return temp & bitboard;
    }

    public long getFile(int fileNumber)
    {
        long temp = Long.parseUnsignedLong("72340172838076673");
        temp = temp << (fileNumber - 1);
        return temp & bitboard;
    }

    public void applyMask(long mask)
    {
        bitboard = bitboard & mask;
    }

    @Override
    public String toString()
    {
        return Long.toString(bitboard);
    }

}
