package com.tvtaobao.android.marketgames.dfw.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.tvtaobao.android.marketgames.dfw.DfwConfig;
import com.tvtaobao.android.marketgames.dfw.wares.IBoGameData;
import com.tvtaobao.android.marketgames.dfw.wares.IImageLoader;

public class GameResourceManager {
    Context context;
    public Drawable[] diceAnimBmps = new Drawable[6];
    public Drawable[] diceRltBmps = new Drawable[6];
    public Drawable diceStatic;
    IBoGameData gameData;
    public Bitmap gameSceneBmp;
    IImageLoader imgLoader;
    public Bitmap playerBmp;
    public Bitmap ultimatePrizeBmp;

    public GameResourceManager(Context context2, IImageLoader imgLoader2, IBoGameData gameData2) {
        this.context = context2;
        this.imgLoader = imgLoader2;
        this.gameData = gameData2;
    }

    /* access modifiers changed from: package-private */
    public void loadResource() {
        if (!DfwConfig.test_loadTimeOut && this.imgLoader != null && this.gameData != null) {
            this.imgLoader.load(this.context, this.gameData.getGameSceneImgUrl(), new IImageLoader.ImgLoadListener() {
                public void onSuccess(Bitmap bitmap) {
                    GameResourceManager.this.gameSceneBmp = bitmap;
                }

                public void onFailed() {
                }
            });
            this.imgLoader.load(this.context, this.gameData.getGamePlayerImgUrl(), new IImageLoader.ImgLoadListener() {
                public void onSuccess(Bitmap bitmap) {
                    GameResourceManager.this.playerBmp = bitmap;
                }

                public void onFailed() {
                }
            });
            this.imgLoader.load(this.context, this.gameData.getDiceStaticImgUrl(), new IImageLoader.ImgLoadListener() {
                public void onSuccess(Bitmap bitmap) {
                    GameResourceManager.this.diceStatic = new BitmapDrawable(bitmap);
                }

                public void onFailed() {
                }
            });
            this.imgLoader.load(this.context, this.gameData.getDiceAnim1ImgUrl(), new IImageLoader.ImgLoadListener() {
                public void onSuccess(Bitmap bitmap) {
                    GameResourceManager.this.diceAnimBmps[0] = new BitmapDrawable(bitmap);
                }

                public void onFailed() {
                }
            });
            this.imgLoader.load(this.context, this.gameData.getDiceAnim2ImgUrl(), new IImageLoader.ImgLoadListener() {
                public void onSuccess(Bitmap bitmap) {
                    GameResourceManager.this.diceAnimBmps[1] = new BitmapDrawable(bitmap);
                }

                public void onFailed() {
                }
            });
            this.imgLoader.load(this.context, this.gameData.getDiceAnim3ImgUrl(), new IImageLoader.ImgLoadListener() {
                public void onSuccess(Bitmap bitmap) {
                    GameResourceManager.this.diceAnimBmps[2] = new BitmapDrawable(bitmap);
                }

                public void onFailed() {
                }
            });
            this.imgLoader.load(this.context, this.gameData.getDiceAnim4ImgUrl(), new IImageLoader.ImgLoadListener() {
                public void onSuccess(Bitmap bitmap) {
                    GameResourceManager.this.diceAnimBmps[3] = new BitmapDrawable(bitmap);
                }

                public void onFailed() {
                }
            });
            this.imgLoader.load(this.context, this.gameData.getDiceAnim5ImgUrl(), new IImageLoader.ImgLoadListener() {
                public void onSuccess(Bitmap bitmap) {
                    GameResourceManager.this.diceAnimBmps[4] = new BitmapDrawable(bitmap);
                }

                public void onFailed() {
                }
            });
            this.imgLoader.load(this.context, this.gameData.getDiceAnim6ImgUrl(), new IImageLoader.ImgLoadListener() {
                public void onSuccess(Bitmap bitmap) {
                    GameResourceManager.this.diceAnimBmps[5] = new BitmapDrawable(bitmap);
                }

                public void onFailed() {
                }
            });
            this.imgLoader.load(this.context, this.gameData.getDiceRlt1ImgUrl(), new IImageLoader.ImgLoadListener() {
                public void onSuccess(Bitmap bitmap) {
                    GameResourceManager.this.diceRltBmps[0] = new BitmapDrawable(bitmap);
                }

                public void onFailed() {
                }
            });
            this.imgLoader.load(this.context, this.gameData.getDiceRlt2ImgUrl(), new IImageLoader.ImgLoadListener() {
                public void onSuccess(Bitmap bitmap) {
                    GameResourceManager.this.diceRltBmps[1] = new BitmapDrawable(bitmap);
                }

                public void onFailed() {
                }
            });
            this.imgLoader.load(this.context, this.gameData.getDiceRlt3ImgUrl(), new IImageLoader.ImgLoadListener() {
                public void onSuccess(Bitmap bitmap) {
                    GameResourceManager.this.diceRltBmps[2] = new BitmapDrawable(bitmap);
                }

                public void onFailed() {
                }
            });
            this.imgLoader.load(this.context, this.gameData.getDiceRlt4ImgUrl(), new IImageLoader.ImgLoadListener() {
                public void onSuccess(Bitmap bitmap) {
                    GameResourceManager.this.diceRltBmps[3] = new BitmapDrawable(bitmap);
                }

                public void onFailed() {
                }
            });
            this.imgLoader.load(this.context, this.gameData.getDiceRlt5ImgUrl(), new IImageLoader.ImgLoadListener() {
                public void onSuccess(Bitmap bitmap) {
                    GameResourceManager.this.diceRltBmps[4] = new BitmapDrawable(bitmap);
                }

                public void onFailed() {
                }
            });
            this.imgLoader.load(this.context, this.gameData.getDiceRlt6ImgUrl(), new IImageLoader.ImgLoadListener() {
                public void onSuccess(Bitmap bitmap) {
                    GameResourceManager.this.diceRltBmps[5] = new BitmapDrawable(bitmap);
                }

                public void onFailed() {
                }
            });
            this.imgLoader.load(this.context, this.gameData.getGameUltimatePrizeImgUrl(), new IImageLoader.ImgLoadListener() {
                public void onSuccess(Bitmap bitmap) {
                    GameResourceManager.this.ultimatePrizeBmp = bitmap;
                }

                public void onFailed() {
                }
            });
        }
    }

    public boolean isDiceLoadFinis() {
        for (Drawable drawable : this.diceAnimBmps) {
            if (drawable == null) {
                return false;
            }
        }
        for (Drawable drawable2 : this.diceRltBmps) {
            if (drawable2 == null) {
                return false;
            }
        }
        if (this.diceStatic != null) {
            return true;
        }
        return false;
    }

    public boolean isAllResLoadFinished() {
        for (Drawable drawable : this.diceAnimBmps) {
            if (drawable == null) {
                return false;
            }
        }
        for (Drawable drawable2 : this.diceRltBmps) {
            if (drawable2 == null) {
                return false;
            }
        }
        if (this.diceStatic == null || this.gameSceneBmp == null || this.playerBmp == null || this.ultimatePrizeBmp == null) {
            return false;
        }
        return true;
    }

    public float getPercent() {
        int count = 0;
        for (Drawable drawable : this.diceAnimBmps) {
            if (drawable != null) {
                count++;
            }
        }
        for (Drawable drawable2 : this.diceRltBmps) {
            if (drawable2 != null) {
                count++;
            }
        }
        if (this.diceStatic != null) {
            count++;
        }
        if (this.gameSceneBmp != null) {
            count++;
        }
        if (this.playerBmp != null) {
            count++;
        }
        if (this.ultimatePrizeBmp != null) {
            count++;
        }
        return ((float) count) / 16.0f;
    }
}
