package anet.channel.monitor;

import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;

class NetWorkKalmanFilter {
    private static final String TAG = "speed.NetWorkKalmanFilter";
    private double Kalman_C1 = ClientTraceData.b.f47a;
    private double Kalman_C2 = ClientTraceData.b.f47a;
    private long Kalman_Count = 0;
    private double Kalman_ek;
    private double Kalman_z;
    private double kalman_Kk;
    private double kalman_Pk;
    private double kalman_Q;
    private double kalman_R;
    private double kalman_Xk;
    private double mcurrentNetWorkSpeed = ClientTraceData.b.f47a;

    NetWorkKalmanFilter() {
    }

    public double addMeasurement(double Measurement, double TimeInMs) {
        double speed_tmp = Measurement / TimeInMs;
        if (speed_tmp >= 8.0d) {
            if (this.Kalman_Count == 0) {
                this.Kalman_C1 = speed_tmp;
                this.kalman_Xk = this.Kalman_C1;
                this.kalman_R = this.kalman_Xk * 0.1d;
                this.kalman_Q = this.kalman_Xk * 0.02d;
                this.kalman_Pk = 0.1d * this.kalman_Xk * this.kalman_Xk;
            } else if (this.Kalman_Count == 1) {
                this.Kalman_C2 = speed_tmp;
                this.kalman_Xk = this.Kalman_C2;
            } else {
                double Kalman_u = speed_tmp - this.Kalman_C2;
                this.Kalman_C1 = this.Kalman_C2;
                this.Kalman_C2 = speed_tmp;
                this.Kalman_z = speed_tmp / 0.95d;
                this.Kalman_ek = this.Kalman_z - (this.kalman_Xk * 0.95d);
                int MeasurementConstraintFlag = 0;
                double tmp_srqt_kalman_R = Math.sqrt(this.kalman_R);
                if (this.Kalman_ek >= 4.0d * tmp_srqt_kalman_R) {
                    MeasurementConstraintFlag = 1;
                    this.Kalman_ek = (0.75d * this.Kalman_ek) + (2.0d * tmp_srqt_kalman_R);
                } else if (this.Kalman_ek <= -4.0d * tmp_srqt_kalman_R) {
                    MeasurementConstraintFlag = 2;
                    this.Kalman_ek = (-1.0d * tmp_srqt_kalman_R) + (0.75d * this.Kalman_ek);
                }
                this.kalman_R = Math.min(Math.max(Math.abs((1.05d * this.kalman_R) - ((0.0025d * this.Kalman_ek) * this.Kalman_ek)), 0.8d * this.kalman_R), 1.25d * this.kalman_R);
                this.kalman_Kk = this.kalman_Pk / (((0.95d * 0.95d) * this.kalman_Pk) + this.kalman_R);
                this.kalman_Xk = this.kalman_Xk + ((1.0d / 0.95d) * Kalman_u) + (this.kalman_Kk * this.Kalman_ek);
                if (MeasurementConstraintFlag == 1) {
                    this.kalman_Xk = Math.min(this.kalman_Xk, this.Kalman_z);
                } else if (MeasurementConstraintFlag == 2) {
                    this.kalman_Xk = Math.max(this.kalman_Xk, this.Kalman_z);
                }
                this.kalman_Pk = (1.0d - (this.kalman_Kk * 0.95d)) * (this.kalman_Pk + this.kalman_Q);
            }
            if (this.kalman_Xk < ClientTraceData.b.f47a) {
                this.mcurrentNetWorkSpeed = this.Kalman_C2 * 0.7d;
                this.kalman_Xk = this.mcurrentNetWorkSpeed;
            } else {
                this.mcurrentNetWorkSpeed = this.kalman_Xk;
            }
            return this.mcurrentNetWorkSpeed;
        } else if (this.Kalman_Count != 0) {
            return this.mcurrentNetWorkSpeed;
        } else {
            this.mcurrentNetWorkSpeed = speed_tmp;
            return this.mcurrentNetWorkSpeed;
        }
    }

    public void ResetKalmanParams() {
        this.Kalman_Count = 0;
        this.mcurrentNetWorkSpeed = ClientTraceData.b.f47a;
    }
}
