import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {
    private Color normalColor;
    private Color hoverColor;
    private Color pressedColor;
    
    public enum Style {
        PRIMARY(new Color(79, 70, 229), new Color(99, 102, 241), new Color(67, 56, 202), new Color(248, 250, 252)), // #4F46E5, #6366F1, #4338CA, #F8FAFC
        ACCENT(new Color(139, 92, 246), new Color(167, 139, 250), new Color(124, 58, 237), new Color(248, 250, 252)), // #8B5CF6, #A78BFA, #7C3AED, #F8FAFC
        SUCCESS(new Color(34, 197, 94), new Color(74, 222, 128), new Color(22, 163, 74), new Color(248, 250, 252)), // #22C55E, #4ADE80, #16A34A, #F8FAFC
        DANGER(new Color(239, 68, 68), new Color(248, 113, 113), new Color(220, 38, 38), new Color(248, 250, 252)), // #EF4444, #F87171, #DC2626, #F8FAFC
        SECONDARY(new Color(30, 41, 59), new Color(51, 65, 85), new Color(15, 23, 42), new Color(248, 250, 252)); // #1E293B, #334155, #0F172A, #F8FAFC
        
        Color normal, hover, pressed, text;
        Style(Color n, Color h, Color p, Color t) {
            this.normal = n; this.hover = h; this.pressed = p; this.text = t;
        }
    }
    
    public ModernButton(String text, Style style) {
        super(text);
        this.normalColor = style.normal;
        this.hoverColor = style.hover;
        this.pressedColor = style.pressed;
        setForeground(style.text);
        
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        setBackground(normalColor);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    setBackground(hoverColor);
                    repaint();
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled()) {
                    setBackground(normalColor);
                    repaint();
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    setBackground(pressedColor);
                    repaint();
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isEnabled()) {
                    setBackground(hoverColor);
                    repaint();
                }
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        if (!isEnabled()) {
            g2.setColor(new Color(51, 65, 85)); // Slate 700
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        super.paintComponent(g2);
        g2.dispose();
    }
}
