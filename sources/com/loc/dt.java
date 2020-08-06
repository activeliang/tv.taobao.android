package com.loc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: ConnectionServiceManager */
public final class dt {
    public boolean a = false;
    private String b = null;
    private Context c = null;
    private boolean d = true;
    private ServiceConnection e = null;
    private Intent f = new Intent();
    private String g = "com.autonavi.minimap";
    private String h = "com.amap.api.service.AMapService";
    private boolean i = false;
    private boolean j = false;
    private boolean k = false;
    private boolean l = false;
    private boolean m = false;
    private boolean n = false;
    private List<Intent> o = new ArrayList();
    private boolean p = false;

    public dt(Context context) {
        this.c = context;
        try {
            this.b = o.b(ec.a(k.f(context).getBytes("UTF-8"), "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDCEYwdO3V2ANrhApjqyk7X8FH5AEaWly58kP9IDAhMqwtIbmcJrUK9oO9Afh3KZnOlDtjiowy733YqpLRO7WBvdbW/c4Dz/d3dy/m+6HMqxaak+GQQRHw/VPdKciaZ3eIZp4MWOyIQwiFSQvPTAo/Na8hV4SgBZHB3lGFw0yu+BmG+h32eIE6p4Y8EDCn+G+yzekX+taMrWTQIysledrygZSGPv1ukbdFDnH/xZEI0dCr9pZT+AZQl3o9a2aMyuRrHM0oupXKKiYl69Y8fKh1Tyd752rF6LrR5uOb9aOfXt18hb+3YL5P9rQ+ZRYbyHYFaxzBPA2jLq0KUQ+Dmg7YhAgMBAAECggEAL9pj0lF3BUHwtssNKdf42QZJMD0BKuDcdZrLV9ifs0f54EJY5enzKw8j76MpdV8N5QVkNX4/BZR0bs9uJogh31oHFs5EXeWbb7V8P7bRrxpNnSAijGBWwscQsyqymf48YlcL28949ujnjoEz3jQjgWOyYnrCgpVhphrQbCGmB5TcZnTFvHfozt/0tzuMj5na5lRnkD0kYXgr0x/SRZcPoCybSpc3t/B/9MAAboGaV/QQkTotr7VOuJfaPRjvg8rzyPzavo3evxsjXj7vDXbN4w0cbk/Uqn2JtvPQ8HoysmF2HdYvILZibvJmWH1hA58b4sn5s6AqFRjMOL7rHdD+gQKBgQD+IzoofmZK5tTxgO9sWsG71IUeshQP9fe159jKCehk1RfuIqqbRP0UcxJiw4eNjHs4zU0HeRL3iF5XfUs0FQanO/pp6YL1xgVdfQlDdTdk6KFHJ0sUJapnJn1S2k7IKfRKE1+rkofSXMYUTsgHF1fDp+gxy4yUMY+h9O+JlKVKOwKBgQDDfaDIblaSm+B0lyG//wFPynAeGd0Q8wcMZbQQ/LWMJZhMZ7fyUZ+A6eL/jB53a2tgnaw2rXBpMe1qu8uSpym2plU0fkgLAnVugS5+KRhOkUHyorcbpVZbs5azf7GlTydR5dI1PHF3Bncemoa6IsEvumHWgQbVyTTz/O9mlFafUwKBgQCvDebms8KUf5JY1F6XfaCLWGVl8nZdVCmQFKbA7Lg2lI5KS3jHQWsupeEZRORffU/3nXsc1apZ9YY+r6CYvI77rRXd1KqPzxos/o7d96TzjkZhc9CEjTlmmh2jb5rqx/Ns/xFcZq/GGH+cx3ODZvHeZQ9NFY+9GLJ+dfB2DX0ZtwKBgQC+9/lZ8telbpqMqpqwqRaJ8LMn5JIdHZu0E6IcuhFLr+ogMW3zTKMpVtGGXEXi2M/TWRPDchiO2tQX4Q5T2/KW19QCbJ5KCwPWiGF3owN4tNOciDGh0xkSidRc0xAh8bnyejSoBry8zlcNUVztdkgMLOGonvCjZWPSOTNQnPYluwKBgCV+WVftpTk3l+OfAJTaXEPNYdh7+WQjzxZKjUaDzx80Ts7hRo2U+EQT7FBjQQNqmmDnWtujo5p1YmJC0FT3n1CVa7g901pb3b0RcOziYWAoJi0/+kLyeo6XBhuLeZ7h90S70GGh1o0V/j/9N1jb5DCL4xKkvdYePPTSTku0BM+n"));
        } catch (Throwable th) {
            en.a(th, "ConnectionServiceManager", "ConnectionServiceManager");
        }
    }

    public final void a() {
        try {
            if (this.e != null && this.l) {
                this.c.unbindService(this.e);
            }
        } catch (Throwable th) {
            en.a(th, "ConnectionServiceManager", "unbindService connService");
        }
        if (this.o != null && this.o.size() > 0) {
            for (Intent stopService : this.o) {
                this.c.stopService(stopService);
            }
        }
        this.c = null;
        this.e = null;
        this.d = true;
        this.a = false;
        this.i = false;
        this.j = false;
        this.k = false;
        this.p = false;
        this.l = false;
        this.m = false;
        this.n = false;
        this.o.clear();
        this.o = null;
    }

    public final void b() {
        try {
            if (this.e == null) {
                this.e = new ServiceConnection() {
                    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        dt.this.a = true;
                    }

                    public final void onServiceDisconnected(ComponentName componentName) {
                        dt.this.a = false;
                    }
                };
            }
        } catch (Throwable th) {
            en.a(th, "ConnectionServiceManager", "init");
        }
    }

    public final void c() {
        ArrayList<String> f2;
        if (!this.p) {
            if (em.b(this.c)) {
                this.f.putExtra("appkey", this.b);
                this.f.setComponent(new ComponentName(this.g, this.h));
                try {
                    this.i = this.c.bindService(this.f, this.e, 1);
                } catch (Throwable th) {
                }
                try {
                    if (!this.i && (f2 = em.f()) != null) {
                        Iterator<String> it = f2.iterator();
                        while (it.hasNext()) {
                            String next = it.next();
                            if (!next.equals(this.h)) {
                                this.f.setComponent(new ComponentName(this.g, next));
                                try {
                                    this.i = this.c.bindService(this.f, this.e, 1);
                                } catch (Throwable th2) {
                                }
                                if (this.i) {
                                    break;
                                }
                            }
                        }
                    }
                    this.l = true;
                } catch (Throwable th3) {
                }
            }
            this.p = true;
        }
    }
}
