from kivy.app import App
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.button import Button
from kivy.uix.label import Label
from kivy.uix.textinput import TextInput
from kivy.uix.spinner import Spinner
from kivy.uix.webview import WebView
from kivy.clock import Clock
import threading
import time
import random

# ======== إعدادات التطبيق ========
QUOTEX_URL = "https://qxbroker.com/en/trade"
MAX_TRADES = 3
MIN_CONFIDENCE = 80  # عتبة منخفضة للتجربة

# ======== تحليل وهمي (للتجربة فقط) ========
def analyze_market():
    """تحليل وهمي - سنستبدله بالتحليل الحقيقي لاحقاً"""
    signals = ["up", "down", "wait"]
    weights = [0.35, 0.35, 0.3]
    signal = random.choices(signals, weights=weights)[0]
    confidence = 80 + random.randint(0, 18)
    return signal, confidence

# ======== واجهة التطبيق ========
class QuotexBotApp(App):
    def build(self):
        main_layout = BoxLayout(orientation='vertical', spacing=5, padding=10)
        
        # شريط التحكم
        control_layout = BoxLayout(orientation='horizontal', size_hint=(1, 0.12), spacing=5)
        self.start_btn = Button(text="▶ بدء", background_color=(0, 0.8, 0, 1), size_hint=(0.25, 1))
        self.start_btn.bind(on_press=self.start_trading)
        self.stop_btn = Button(text="⏹ إيقاف", background_color=(0.8, 0, 0, 1), size_hint=(0.25, 1))
        self.stop_btn.bind(on_press=self.stop_trading)
        self.stop_btn.disabled = True
        self.status_label = Label(text="جاهز", size_hint=(0.5, 1))
        control_layout.add_widget(self.start_btn)
        control_layout.add_widget(self.stop_btn)
        control_layout.add_widget(self.status_label)
        main_layout.add_widget(control_layout)
        
        # إعدادات (حساب، مبلغ)
        settings_layout = BoxLayout(orientation='horizontal', size_hint=(1, 0.1), spacing=5)
        self.account_spinner = Spinner(text="تجريبي", values=["تجريبي", "حقيقي"], size_hint=(0.4, 1))
        self.amount_input = TextInput(text="10", input_filter='float', size_hint=(0.3, 1))
        settings_layout.add_widget(Label(text="الحساب:", size_hint=(0.2, 1)))
        settings_layout.add_widget(self.account_spinner)
        settings_layout.add_widget(Label(text="المبلغ:", size_hint=(0.2, 1)))
        settings_layout.add_widget(self.amount_input)
        main_layout.add_widget(settings_layout)
        
        # WebView للمنصة
        self.webview = WebView(url=QUOTEX_URL, size_hint=(1, 0.6))
        main_layout.add_widget(self.webview)
        
        # شريط الحالة السفلي
        bottom_layout = BoxLayout(orientation='horizontal', size_hint=(1, 0.08), spacing=5)
        self.trade_label = Label(text="📊 الصفقات: 0/3", size_hint=(0.5, 1))
        self.signal_label = Label(text="📈 الإشارة: —", size_hint=(0.5, 1))
        bottom_layout.add_widget(self.trade_label)
        bottom_layout.add_widget(self.signal_label)
        main_layout.add_widget(bottom_layout)
        
        self.is_running = False
        self.trades_count = 0
        self.trading_thread = None
        
        return main_layout
    
    def start_trading(self, instance):
        if self.is_running:
            return
        self.is_running = True
        self.trades_count = 0
        self.start_btn.disabled = True
        self.stop_btn.disabled = False
        self.status_label.text = "يعمل..."
        self.trade_label.text = "📊 الصفقات: 0/3"
        self.signal_label.text = "📈 الإشارة: جاري التحليل..."
        
        self.trading_thread = threading.Thread(target=self.trading_loop)
        self.trading_thread.daemon = True
        self.trading_thread.start()
    
    def stop_trading(self, instance):
        self.is_running = False
        self.start_btn.disabled = False
        self.stop_btn.disabled = True
        self.status_label.text = "متوقف"
        self.signal_label.text = "📈 الإشارة: —"
    
    def trading_loop(self):
        while self.is_running and self.trades_count < MAX_TRADES:
            try:
                signal, confidence = analyze_market()
                signal_name = "صعود ✅" if signal == "up" else "هبوط 🔻" if signal == "down" else "انتظار ⏳"
                
                Clock.schedule_once(lambda dt: self.update_signal(signal_name), 0)
                print(f"📊 إشارة: {signal_name} (الثقة: {confidence}%)")
                
                if signal != "wait" and confidence >= MIN_CONFIDENCE:
                    Clock.schedule_once(lambda dt: self.execute_trade(signal_name), 0)
                    self.trades_count += 1
                    Clock.schedule_once(lambda dt: self.update_trades(), 0)
                    
                    if self.trades_count >= MAX_TRADES:
                        Clock.schedule_once(lambda dt: self.stop_trading(None), 0)
                        print("⏹ تم الوصول للحد الأقصى 3 صفقات")
                        break
                
                wait_time = random.randint(15, 30)
                print(f"⏳ انتظار {wait_time} ثانية...")
                time.sleep(wait_time)
                
            except Exception as e:
                print(f"❌ خطأ: {e}")
                time.sleep(10)
    
    def update_signal(self, signal_name):
        self.signal_label.text = f"📈 الإشارة: {signal_name}"
    
    def update_trades(self):
        self.trade_label.text = f"📊 الصفقات: {self.trades_count}/{MAX_TRADES}"
    
    def execute_trade(self, signal_name):
        self.status_label.text = f"تم فتح {signal_name}"
        print(f"🖱️ تنفيذ صفقة: {signal_name}")

if __name__ == "__main__":
    QuotexBotApp().run()
