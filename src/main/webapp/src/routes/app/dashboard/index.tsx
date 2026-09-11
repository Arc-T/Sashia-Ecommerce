import {
  Package,
  ShoppingCart,
  DollarSign,
  ArrowUpRight,
  ArrowDownRight,
  AlertTriangle,
  Clock,
} from "lucide-react";
import { useTranslation } from "react-i18next";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Link } from "react-router";

// Sample chart & order data
const CHART_DATA = [
  { name: "Jan", revenue: 24000, orders: 40 },
  { name: "Feb", revenue: 13980, orders: 30 },
  { name: "Mar", revenue: 38000, orders: 60 },
  { name: "Apr", revenue: 39080, orders: 45 },
  { name: "May", revenue: 48000, orders: 80 },
  { name: "Jun", revenue: 38000, orders: 65 },
  { name: "Jul", revenue: 43000, orders: 90 },
  { name: "Aug", revenue: 51000, orders: 95 },
];

const STATUS_COLORS = {
  pending: {
    bg: "bg-amber-50 dark:bg-amber-950/40",
    text: "text-amber-700 dark:text-amber-400",
    dot: "bg-amber-400",
  },
  processing: {
    bg: "bg-blue-50 dark:bg-blue-950/40",
    text: "text-blue-700 dark:text-blue-400",
    dot: "bg-blue-400",
  },
  shipped: {
    bg: "bg-indigo-50 dark:bg-indigo-950/40",
    text: "text-indigo-700 dark:text-indigo-400",
    dot: "bg-indigo-400",
  },
  delivered: {
    bg: "bg-emerald-50 dark:bg-emerald-950/40",
    text: "text-emerald-700 dark:text-emerald-400",
    dot: "bg-emerald-400",
  },
  cancelled: {
    bg: "bg-red-50 dark:bg-red-950/40",
    text: "text-red-700 dark:text-red-400",
    dot: "bg-red-400",
  },
  refunded: {
    bg: "bg-slate-100 dark:bg-slate-800",
    text: "text-slate-600 dark:text-slate-400",
    dot: "bg-slate-400",
  },
};

// Simple fade-up animation for cards
const fadeUp = {
  hidden: { opacity: 0, y: 24 },
  show: (i = 0) => ({
    opacity: 1,
    y: 0,
    transition: { delay: i * 0.07, duration: 0.45, ease: [0.22, 1, 0.36, 1] },
  }),
};

// -----------------
// Components
// -----------------
function StatCard({ stat, index }: { stat: any; index: number }) {
  return (
    <Card className="border-0 shadow-sm hover:shadow-lg transition-all duration-300 bg-white dark:bg-slate-900 overflow-hidden group">
      <CardContent className="p-5">
        <div className="flex items-start justify-between">
          <div className="space-y-1">
            <p className="text-[11px] font-semibold uppercase tracking-widest text-slate-400 dark:text-slate-500">
              {stat.label}
            </p>
            <p className="text-3xl font-bold text-slate-900 dark:text-white tracking-tight">
              {stat.value}
            </p>
            <div className="flex items-center gap-1 pt-1">
              {stat.up ? (
                <ArrowUpRight className="w-3.5 h-3.5 text-emerald-500" />
              ) : (
                <ArrowDownRight className="w-3.5 h-3.5 text-red-500" />
              )}
              <span
                className={`text-xs font-semibold ${stat.up ? "text-emerald-500" : "text-red-500"}`}
              >
                {stat.change}
              </span>
              <span className="text-[11px] text-slate-400 ms-1">
                vs last month
              </span>
            </div>
          </div>
          <div
            className={`w-12 h-12 rounded-2xl bg-linear-to-br ${stat.gradient} flex items-center justify-center shadow-lg group-hover:scale-110 transition-transform duration-300`}
          >
            <stat.icon className="w-5 h-5 text-white" />
          </div>
        </div>
      </CardContent>
    </Card>
  );
}

function AlertCard({
  type,
  icon,
  message,
  subMessage,
  link,
}: {
  type: "warning" | "info";
  icon: React.ReactNode;
  message: string;
  subMessage?: string;
  link: { href: string; label: string };
}) {
  const typeStyles = {
    warning:
      "bg-amber-50 dark:bg-amber-950/30 border-amber-200 dark:border-amber-800/50",
    info: "bg-blue-50 dark:bg-blue-950/30 border-blue-200 dark:border-blue-800/50",
  } as const;
  return (
    <div
      className={`flex items-center gap-3 p-3.5 rounded-xl border ${typeStyles[type]}`}
    >
      {icon}
      <div className="flex-1 min-w-0">
        <p
          className={`text-sm font-semibold ${type === "warning" ? "text-amber-800 dark:text-amber-300" : "text-blue-800 dark:text-blue-300"}`}
        >
          {message}
        </p>
        {subMessage && (
          <p
            className={`text-xs ${type === "warning" ? "text-amber-600 dark:text-amber-400/70" : "text-blue-600 dark:text-blue-400/70"}`}
          >
            {subMessage}
          </p>
        )}
      </div>
      <Link to={link.href}>
        <Button
          size="sm"
          variant="outline"
          className={`text-xs shrink-0 ${type === "warning" ? "text-amber-700 border-amber-300 hover:bg-amber-100" : "text-blue-700 border-blue-300 hover:bg-blue-100"}`}
        >
          {link.label}
        </Button>
      </Link>
    </div>
  );
}

// -----------------
// Main Component
// -----------------
export function DashboardIndex() {
  const { t } = useTranslation();

  // Fetch products & orders
  const products = [];
  const orders = [];

  const totalRevenue = orders
    .filter((o) => o.payment_status === "paid")
    .reduce((s, o) => s + (o.total || 0), 0);
  const lowStockProducts = products.filter((p) => (p.stock || 0) < 10);
  const pendingOrders = orders.filter((o) => o.status === "pending");

  const stats = [
    {
      label: t("total_revenue"),
      value: `$${totalRevenue.toLocaleString()}`,
      icon: DollarSign,
      gradient: "from-violet-500 to-purple-600",
      change: "+23%",
      up: true,
    },
    {
      label: t("total_orders"),
      value: orders.length,
      icon: ShoppingCart,
      gradient: "from-blue-500 to-indigo-600",
      change: "+8%",
      up: true,
    },
    {
      label: t("total_products"),
      value: products.length,
      icon: Package,
      gradient: "from-emerald-500 to-teal-600",
      change: "+12%",
      up: true,
    },
    {
      label: "Low Stock",
      value: lowStockProducts.length,
      icon: AlertTriangle,
      gradient: "from-amber-500 to-orange-500",
      change: "-3",
      up: false,
    },
  ];

  const orderStatusCounts = [
    "pending",
    "processing",
    "shipped",
    "delivered",
    "cancelled",
  ].map((s) => ({
    status: s,
    count: orders.filter((o) => o.status === s).length,
  }));

  return (
    <>
      {/* Stats */}
      <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-4">
        {stats.map((stat, i) => (
          <StatCard key={i} stat={stat} index={i} />
        ))}
      </div>

      {/* Alerts */}
      {(lowStockProducts.length || pendingOrders.length) > 0 && (
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
          {lowStockProducts.length > 0 && (
            <AlertCard
              type="warning"
              icon={<AlertTriangle className="w-5 h-5 text-amber-500" />}
              message={`${lowStockProducts.length} products low on stock`}
              link={{ href: "Products", label: "View" }}
            />
          )}
          {pendingOrders.length > 0 && (
            <AlertCard
              type="info"
              icon={<Clock className="w-5 h-5 text-blue-500" />}
              message={`${pendingOrders.length} orders awaiting processing`}
              subMessage="Requires your attention"
              link={{ href: "Orders", label: "View" }}
            />
          )}
        </div>
      )}

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* <RevenueChart data={CHART_DATA} className="lg:col-span-2" />
          <OrdersByStatusChart data={orderStatusCounts} /> */}
      </div>
    </>
  );
}
