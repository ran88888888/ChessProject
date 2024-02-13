package main;

public class BitBoard {
    public long bitboard;

    public BitBoard()
    {
        bitboard = 0L;
    }

    public BitBoard(long number)
    {
        bitboard = number;
    }

    public int bitSetCount()
    {
        return Long.bitCount(bitboard);
    }

    public void clearBit(int position)
    {
        long temp = 1L;
        temp = ~(temp << position);
        bitboard = bitboard & temp;
    }

    public void toggleBit(int position)
    {
        long temp = 1L;
        temp = temp << position;
        bitboard = bitboard ^ temp;
    }

    public boolean isBitSet(int position)
    {
        long temp = 1L;
        temp = temp << position;
        return (bitboard & temp) != 0;
    }

    public String toBinaryString()
    {
        return Long.toBinaryString(bitboard);
    }

    public long getRank(int rankNumber)
    {
        long temp = 255L;
        temp = temp << (8 * (rankNumber - 1));
        return temp & bitboard;
    }

    public long getFile(int fileNumber)
    {
        long temp = 72340172838076673L;
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
