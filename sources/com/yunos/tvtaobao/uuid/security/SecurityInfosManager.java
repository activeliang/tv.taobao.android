package com.yunos.tvtaobao.uuid.security;

import android.content.Context;
import com.yunos.tvtaobao.uuid.TVAppUUIDImpl;
import com.yunos.tvtaobao.uuid.infos.InfosManager;
import com.yunos.tvtaobao.uuid.utils.Logger;
import com.yunos.tvtaobao.uuid.utils.SGMWrapper;

public class SecurityInfosManager {
    private String mPK = "308204bd020100300d06092a864886f70d0101010500048204a7308204a30201000282010100995b30c6f8fdb9a2562bdcb2c08a06e835a3f9946441974f2b73343d0a7c45567e13cb6a468eeaeea386b63ada102c3d2b6a27bfbd829f00a2f4f76035e27191ec69e62b01f27d7e06f023953472bafdfd40e045e1c60e5fe3b662e87c35ccddaf2f83470fc5095051131388cc34845309f043adc659301e87efdcb3921ba20d6408980606d36cdd2257f0cf9b4203186e98e0f283fa6687a33978cbc9e76cdb2395c9ffe447357445377548712fa497b39977cd6d52e872f66ae8d811dc9e91c314e035d2b103688761177358724686e022254086f90719e07dbd68ac6663642b6b0d685953dfa923e64bd5444b75e08deb562bbe3b7a6a6bdd9c57de9e1da102010302820100663ccb2f50a9266c39729321d5b159f023c2a662ed810f8a1cf77828b1a82e39a962879c2f09f1f46d04797c91601d7e1cf16fd52901bf55c1f8a4eace96f66148469972014c53a959f56d0e22f72753fe2b402e9684099542799745a823dde91f75022f5fd8b0e0360cb7b0882302e206a02d1e843b7569aff53dcd0c126c07e50dc2fc6c6a3c17a07b1da923bd0a7de1f1a7a60acabe0b6119ac9e203246d7317c92696f2b10ff7d22fc1ed8f5bdcc317340a210ab5ccaef6ca7fbc6c9e0a48eb1a7b0f11bd44662f945e1ad0109c61fc4c8a7d618901f368f4e229cdfd6dcdd34f5ee2580ca72466be8351f23a0c19520bd57045ee2b49846fa1f01efe6cb02818100cc1a8c20fa6ca2a3613f518c2c1add81c73b90a0327509dedffcd8f345d8402b625857b654423b79d48a80a328cd372120d9a391faf87dc3942149aa0eb0939e60abfc34873883cd38807ed7b93ae0e29f6de5739d4c67c47ab2730be4424a62dfc754ed08f3e72697d37b4be0de7b00388abdd8a9bf6ffedf3b059030ab307502818100c059676a69c77016505ff2c5b98b95d9d472d4d941553f97b1961ceb53c3c26cf70296ab6944607b34f87a7702f1d0c44892f34859595f7efb26a33458fd39fc8c5e6877e1cec131ba6aafc91bb5d6fb110d12d12887c72693f45528dcd456b5ffd44796181ec8d72270f439b4b789bdf5af7c508dedb65ca8381f992b0b12fd028181008811b2c0a6f31717962a365d72bc93abda27b5c021a35be9eaa890a22e902ac796e58fcee2d6d251385c55c21b337a1615e66d0bfca5a92d0d6b86715f20626995c7fd785a25ad337b00548fd0d1eb41bf9e98f7be32efd851cc4cb2982c31973fda389e05f7ef6f0fe2523295e9a7557b07293b1bd4f55494d203b575c775a302818100803b9a46f12fa00ee03ff72e7bb263e68da1e33b80e37fba766413478d2d2c48a4ac6472462d95a778a5a6fa01f68b2d85b74cdae63b94ff5219c222e5fe26a85d9445a5413480cbd19c7530bd23e4a760b361e0c5afda19b7f838c5e88d8f23ffe2da64101485e4c1a0a2d12325067ea3ca52e05e9e79931ad01510c75cb753028181008b20cea8091310941f45264e6a8407e7e7a10d54ada8137f94ca08cdf167c2a206e52eacbb5b2d4e12f437b6cb47fcc406155b6746ed1901bc95a4396c143765a851200be3954f99a91f0feafeab13e31cd90a7140e28c3df593eca62fc4b8af1cc458c309a4c2bbdb410087e6a6b8a1986fc9e2ba67f1610b630968c13ff1e6";
    private String mPKMd5 = "7728017ee852110b5366c7fff9acde1a";
    private String mPassWord = "sonuy_20130110";
    private String mSignedData;

    public SecurityInfosManager(InfosManager infosManager, Context context) {
        SGMWrapper sgm = new SGMWrapper(context, TVAppUUIDImpl.mSMGAuthcode);
        this.mPKMd5 = sgm.getPKMd5();
        if (this.mPKMd5 == null) {
            Logger.loge("can not get pkmd5");
        }
        this.mPassWord = sgm.getPassword();
        if (this.mPassWord == null) {
            Logger.loge("can not get password");
        }
        this.mPK = sgm.getPrivateKey();
        if (this.mPK == null) {
            Logger.loge("can not get pk");
        }
        this.mSignedData = new Signature().sign(this.mPK, infosManager.getAllInfos() + this.mPassWord);
        Logger.loge("sign over");
    }

    public String getSignedData() {
        return this.mSignedData;
    }

    public String getPKMd5() {
        return this.mPKMd5;
    }
}
