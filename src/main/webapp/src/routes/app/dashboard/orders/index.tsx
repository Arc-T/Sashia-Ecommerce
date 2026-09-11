import { createFileRoute } from "@tanstack/react-router";
import { useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { ShoppingCart, Eye } from "lucide-react";
import { format } from "date-fns";
import { useTranslation } from "react-i18next";
import PageWrapper from "@/components/custom/page-wrappers";
import Breadcrumb from "src/components/custom/auto-breadcrumb";
import PageHeader from "src/components/custom/page-header";
import DataTable from "@/components/custom/data-table";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "src/components/ui/dialog";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
} from "src/components/ui/select";

export const Route = createFileRoute("/(app)/(authenticated)/dashboard/orders/")({
  component: OrdersIndex,
});

function OrdersIndex() {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const [selected, setSelected] = useState(null);

  const { data: orders = [], isLoading } = useQuery({
    queryKey: ["orders"],
    queryFn: () => console.log("hello"),
  });

  const updateMutation = useMutation({
    mutationFn: () => console.log("hello"),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["orders"] }),
  });

  const columns = [
    {
      accessorKey: "order_number",
      header: t("order_number"),
      cell: (row) => (
        <span className="font-mono font-semibold text-indigo-600 dark:text-indigo-400">
          #{row.order_number}
        </span>
      ),
    },
    { accessorKey: "customer_name", header: t("customer") },
    {
      accessorKey: "total",
      header: t("total"),
      cell: (row) => (
        <span className="font-semibold">${row.total?.toLocaleString()}</span>
      ),
    },
    {
      accessorKey: "status",
      header: t("status"),
      cell: (row) => <StatusBadge status={row.status ?? "pending"} />,
    },
    {
      accessorKey: "payment_status",
      header: t("payment_status"),
      cell: (row) => <StatusBadge status={row.payment_status ?? "unpaid"} />,
    },
    {
      accessorKey: "created_date",
      header: t("start_date"),
      cell: (row) =>
        row.created_date
          ? format(new Date(row.created_date), "MMM d, yyyy")
          : "—",
    },
    {
      id: "actions",
      header: t("actions"),
      cell: (row) => (
        <Button
          variant="ghost"
          size="icon"
          className="h-8 w-8"
          onClick={(e) => {
            e.stopPropagation();
            setSelected(row);
          }}
        >
          <Eye className="w-3.5 h-3.5" />
        </Button>
      ),
    },
  ];

  const filters = [
    {
      key: "status",
      label: t("status"),
      options: [
        "pending",
        "processing",
        "shipped",
        "delivered",
        "cancelled",
        "refunded",
      ].map((s) => ({ value: s, label: t(s) })),
    },
    {
      key: "payment_status",
      label: t("payment_status"),
      options: ["unpaid", "paid", "refunded"].map((s) => ({
        value: s,
        label: t(s),
      })),
    },
  ];

  return (
    <PageWrapper>
      <Breadcrumb items={[{ label: t("sales") }, { label: t("orders") }]} />
      <PageHeader title={t("orders")} />
      <DataTable
        columns={columns}
        data={orders}
        isLoading={isLoading}
        filters={filters}
        emptyIcon={<ShoppingCart className="w-10 h-10" />}
      />

      <Dialog open={!!selected} onOpenChange={() => setSelected(null)}>
        <DialogContent className="sm:max-w-lg">
          <DialogHeader>
            <DialogTitle>
              {t("order")} #{selected?.order_number}
            </DialogTitle>
          </DialogHeader>
          {selected && (
            <div className="space-y-4 py-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <p className="text-xs text-slate-400">{t("customer")}</p>
                  <p className="font-medium">{selected.customer_name}</p>
                  <p className="text-sm text-slate-500">
                    {selected.customer_email}
                  </p>
                  <p className="text-sm text-slate-500">
                    {selected.customer_phone}
                  </p>
                </div>
                <div>
                  <p className="text-xs text-slate-400">{t("total")}</p>
                  <p className="text-xl font-bold">
                    ${selected.total?.toLocaleString()}
                  </p>
                  {selected.discount_amount > 0 && (
                    <p className="text-sm text-emerald-600">
                      -${selected.discount_amount} {t("discount")}
                    </p>
                  )}
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <p className="text-xs text-slate-400">{t("status")}</p>
                  <Select
                    value={selected.status || "pending"}
                    onValueChange={(v) => {
                      updateMutation.mutate({
                        id: selected.id,
                        data: { status: v },
                      });
                      setSelected({ ...selected, status: v });
                    }}
                  >
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      {[
                        "pending",
                        "processing",
                        "shipped",
                        "delivered",
                        "cancelled",
                        "refunded",
                      ].map((s) => (
                        <SelectItem key={s} value={s}>
                          {t(s)}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <div className="space-y-2">
                  <p className="text-xs text-slate-400">
                    {t("payment_status")}
                  </p>
                  <Select
                    value={selected.payment_status || "unpaid"}
                    onValueChange={(v) => {
                      updateMutation.mutate({
                        id: selected.id,
                        data: { payment_status: v },
                      });
                      setSelected({ ...selected, payment_status: v });
                    }}
                  >
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      {["unpaid", "paid", "refunded"].map((s) => (
                        <SelectItem key={s} value={s}>
                          {t(s)}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
              </div>

              {selected.items?.length > 0 && (
                <div>
                  <p className="text-xs text-slate-400 mb-2">{t("products")}</p>
                  <div className="space-y-2">
                    {selected.items.map((item, i) => (
                      <div
                        key={i}
                        className="flex justify-between p-3 rounded-lg bg-slate-50 dark:bg-slate-800"
                      >
                        <div>
                          <p className="text-sm font-medium">
                            {item.product_name}
                          </p>
                          <p className="text-xs text-slate-400">
                            × {item.quantity}
                          </p>
                        </div>
                        <p className="text-sm font-semibold">
                          ${item.total?.toLocaleString()}
                        </p>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {selected.shipping_address && (
                <div>
                  <p className="text-xs text-slate-400">{t("address")}</p>
                  <p className="text-sm">{selected.shipping_address}</p>
                </div>
              )}

              {selected.notes && (
                <div>
                  <p className="text-xs text-slate-400">{t("notes")}</p>
                  <p className="text-sm">{selected.notes}</p>
                </div>
              )}
            </div>
          )}
        </DialogContent>
      </Dialog>
    </PageWrapper>
  );
}
