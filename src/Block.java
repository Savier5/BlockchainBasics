import java.util.ArrayList;


/**
 * This holds the values for a Block in the Blockchain, and it has methods to compute the Merkle Root and generate a hash.
 */
public class Block {


    private String sMerkleRoot;
    private int iDifficulty = 5; // Mining seconds in testing 5: 6,10,15,17,20,32 | testing 6: 12,289,218
    private String sNonce;
    private String sMinerUsername;
    private String sHash;



    /**
     * This computes the Merkle Root. It either accepts 2 or 4 items, or if made to be dynamic, then accepts any
     * multiple of 2 (2,4,8.16.32,etc.) items.
     * @param lstItems
     * @return
     */
    public synchronized String computeMerkleRoot(ArrayList<String> lstItems) {

        MerkleNode oNode11 = new MerkleNode();
        MerkleNode oNode22 = new MerkleNode();
        MerkleNode oNode33 = new MerkleNode();
        MerkleNode oNode44 = new MerkleNode();
        MerkleNode oNode55 = new MerkleNode();
        MerkleNode oNode66 = new MerkleNode();
        MerkleNode oNode7 = new MerkleNode();

        //Works for 2, 4 and 16, haven't made it work any other numbers, just needs a bit more tweaking

        if (lstItems.size() == 2){
            oNode11.sHash = BlockchainUtil.generateHash(lstItems.get(0));
            oNode22.sHash = BlockchainUtil.generateHash(lstItems.get(1));

            populateMerkleNode(oNode7, oNode11, oNode22);

        }else if( lstItems.size() == 4) {

            oNode11.sHash = BlockchainUtil.generateHash(lstItems.get(0));
            oNode22.sHash = BlockchainUtil.generateHash(lstItems.get(1));
            oNode33.sHash = BlockchainUtil.generateHash(lstItems.get(2));
            oNode44.sHash = BlockchainUtil.generateHash(lstItems.get(3));

            populateMerkleNode(oNode55, oNode11, oNode22);
            populateMerkleNode(oNode66, oNode33, oNode44);
            populateMerkleNode(oNode7, oNode55, oNode66);

            return  oNode7.sHash;

        }else {
            MerkleNode oNode1 = new MerkleNode();
            MerkleNode oNode2 = new MerkleNode();
            MerkleNode oNode3 = new MerkleNode();
            MerkleNode oNode4 = new MerkleNode();
            MerkleNode oNode5 = new MerkleNode();
            MerkleNode oNode6 = new MerkleNode();

            ArrayList<String>sNodes = new ArrayList();

            ArrayList<String>sNodes2 = new ArrayList();

            double b = 16;
            int o = 0;
            int p = 0;

            while (o != 1){
                if(b != 1){
                    b = b * 0.5;
                    p++;
                }else{
                    o = 1;
                }
            }

            int r = 0;
            int s = 1;

            for(int h = 0; h < (lstItems.size()*0.5); h++){

                if(h == 0){
                    oNode2.sHash = lstItems.get(r);
                    oNode3.sHash = lstItems.get(s);


                }else{
                    r++;
                    s++;
                    oNode2.sHash = lstItems.get(r + 1);
                    oNode3.sHash = lstItems.get(s + 1);
                    if(s > r){
                        r++;
                        s++;
                    }
                }

                populateMerkleNode(oNode1,oNode2,oNode3);

                sNodes.add(String.valueOf(oNode1.sHash));

            }


            int c = 0;
            while(c < ((p/2)-1)) {

                c++;
                r = 0;
                s = 1;

                for (int h = 0; h < (sNodes.size() * 0.5); h++) {

                    if (h == 0) {
                        oNode2.sHash = sNodes.get(r);
                        oNode3.sHash = sNodes.get(s);
                    } else {
                        r++;
                        s++;
                        oNode2.sHash = sNodes.get(r + 1);
                        oNode3.sHash = sNodes.get(s + 1);
                        if (s > r) {
                            r++;
                            s++;
                        }
                    }

                    populateMerkleNode(oNode1, oNode2, oNode3);

                    sNodes2.add(String.valueOf(oNode1.sHash));

                }
                r = 0;
                s = 1;

                for (int h = 0; h < (sNodes2.size() * 0.5); h++) {

                    if (h == 0) {
                        oNode5.sHash = sNodes2.get(r);
                        oNode6.sHash = sNodes2.get(s);
                    } else {
                        r++;
                        s++;
                        oNode5.sHash = sNodes2.get(r + 1);
                        oNode6.sHash = sNodes2.get(s + 1);
                        if (s > r) {
                            r++;
                            s++;
                        }
                    }

                    populateMerkleNode(oNode4, oNode5, oNode6);

                    sNodes.add(String.valueOf(oNode1.sHash));

                }

                if(c == ((p/2)-1)){

                    populateMerkleNode(oNode6, oNode1, oNode4);

                    sNodes.add(String.valueOf(oNode6.sHash));
                    System.out.println("Final " + sNodes.get(sNodes.size()-1));

                    oNode7.sHash = sNodes.get(sNodes.size()-1);
                }
            }
        }

        return  oNode7.sHash;

//		#####################
//		### ADD CODE HERE ###
//		#####################

    }



    /**
     * This method populates a Merkle node's left, right, and hash variables.
     * @param oNode
     * @param oLeftNode
     * @param oRightNode
     */
    private void populateMerkleNode(MerkleNode oNode, MerkleNode oLeftNode, MerkleNode oRightNode){

        oNode.oLeft = oLeftNode;
        oNode.oRight = oRightNode;
        oNode.sHash = BlockchainUtil.generateHash(oLeftNode.sHash + oRightNode.sHash);

//		#####################
//		### ADD CODE HERE ###
//		#####################
    }


    // Hash this block, and hash will also be next block's previous hash.

    /**
     * This generates the hash for this block by combining the properties and hashing them.
     * @return
     */
    public String computeHash() {

        return new BlockchainUtil().generateHash(sMerkleRoot + iDifficulty + sMinerUsername + sNonce);
    }



    public int getDifficulty() {
        return iDifficulty;
    }


    public String getNonce() {
        return sNonce;
    }
    public void setNonce(String nonce) {
        this.sNonce = nonce;
    }

    public void setMinerUsername(String sMinerUsername) {
        this.sMinerUsername = sMinerUsername;
    }

    public String getHash() { return sHash; }
    public void setHash(String h) {
        this.sHash = h;
    }

    public synchronized void setMerkleRoot(String merkleRoot) { this.sMerkleRoot = merkleRoot; }




    /**
     * Run this to test your merkle tree logic.
     * @param args
     */
    public static void main(String[] args){

        ArrayList<String> lstItems = new ArrayList<>();
        Block oBlock = new Block();
        String sMerkleRoot;

        // These merkle root hashes based on "t1","t2" for two items, and then "t3","t4" added for four items.
        String sExpectedMerkleRoot_2Items = "3269f5f93615478d3d2b4a32023126ff1bf47ebc54c2c96651d2ac72e1c5e235";
        String sExpectedMerkleRoot_4Items = "e08f7b0331197112ff8aa7acdb4ecab1cfb9497cbfb84fb6d54f1cfdb0579d69";

        lstItems.add("t1");
        lstItems.add("t2");


        // *** BEGIN TEST 2 ITEMS ***

        sMerkleRoot = oBlock.computeMerkleRoot(lstItems);

        if(sMerkleRoot.equals(sExpectedMerkleRoot_2Items)){

            System.out.println("Merkle root method for 2 items worked!");
        }

        else{
            System.out.println("Merkle root method for 2 items failed!");
            System.out.println("Expected: " + sExpectedMerkleRoot_2Items);
            System.out.println("Received: " + sMerkleRoot);

        }


        // *** BEGIN TEST 4 ITEMS ***

        lstItems.add("t3");
        lstItems.add("t4");
        sMerkleRoot = oBlock.computeMerkleRoot(lstItems);

        if(sMerkleRoot.equals(sExpectedMerkleRoot_4Items)){

            System.out.println("Merkle root method for 4 items worked!");
        }

        else{
            System.out.println("Merkle root method for 4 items failed!");
            System.out.println("Expected: " + sExpectedMerkleRoot_4Items);
            System.out.println("Received: " + sMerkleRoot);

        }
    }
}
