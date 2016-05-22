package handsome.is.joey.servlet;

public interface IDTypeDefInterface {
	static final int ID = 1;
	static final int FID = 2;
	static final int CID = 3;
	static final int JID = 4;
	static final int AUID = 5;
	static final int AFID = 6;
	static final int RID = 7;
	static final int AFIDANDID = 8;
	static final int AUIDFIDCIDJIDRID = 9;
	static final int AUIDFIDCIDJID = 10;
	static final int AFIDANDIDANDRID = 11;
	static final int AUIDSET = 255;
	static final int HASHMAPSIZESMALL = 64;
	static final int HASHMAPSIZEBIG = 8192;
	static final int EXPRSTRINGLENGTH = 1200;
	static final int EXPRSTRINGLENGTHFORSET = 1200;
	
	static final String[] TYPE = {"","Id","F.FId","C.CId","J.JId","AA.AuId","AA.AfId","RId","AA.AfId,Id","AA.AuId,F.FId,C.CId,J.JId,RId","AA.AuId,F.FId,C.CId,J.JId","AA.AfId,Id,RId"};
}