import { useTranslation } from "react-i18next";
import { AppSidebar } from "@/components/layout/app-sidebar";
import { TopBar } from "@/components/layout/topbar";
import { ThemeProvider } from "@/components/theme-provider";
import { SidebarInset, SidebarProvider } from "@/components/ui/sidebar";
import { Toaster } from "@/components/ui/sonner";
import { useIsMobile } from "@/hooks/use-mobile";
import { Outlet, useLocation } from "react-router";
import { AnimatePresence, motion } from "motion/react";

export function DashboardLayout() {
  const isMobile = useIsMobile();
  const { i18n } = useTranslation();
  const dir = i18n.dir();
  const isRTL = dir === "rtl";
  const location = useLocation();

  return (
    <ThemeProvider defaultTheme="system" storageKey="theme">
      <SidebarProvider defaultOpen={!isMobile}>
        <Toaster
          position="top-center"
          expand
          richColors
          duration={4000}
          dir={dir}
        />

        <div
          className="flex min-h-dvh w-full bg-background antialiased"
          dir={dir}
        >
          <AppSidebar dir={dir} side={isRTL ? "right" : "left"} />

          <SidebarInset className="relative flex flex-1 flex-col">
            <TopBar />

            <main className="relative flex-1 overflow-y-auto p-2 sm:p-4 lg:p-6">
              <AnimatePresence mode="wait">
                <motion.div
                  key={location.pathname}
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ duration: 0.2, ease: "backIn" }}
                >
                  <Outlet />
                </motion.div>
              </AnimatePresence>
            </main>
          </SidebarInset>
        </div>
      </SidebarProvider>
    </ThemeProvider>
  );
}
