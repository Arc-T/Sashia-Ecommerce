import * as React from "react";
import { Edit, LucideEye, MoreHorizontal, Trash2 } from "lucide-react";

import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

import { DeleteConfirmationDialog } from "../elements/destructive-alert";
import { useTranslation } from "react-i18next";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router";

interface Props<T extends { id: number; name: string }> {
  item: T;
  resource: string;
  remove: (id: number) => Promise<void>;
}

export function ActionsColumn<T extends { id: number; name: string }>({
  item,
  resource,
  remove,
}: Props<T>) {
  const { id, name } = item;
  const { t, i18n } = useTranslation();
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  const { isPending, mutate: deleteResource } = useMutation({
    mutationFn: remove,
    onSuccess: () =>
      queryClient.invalidateQueries({
        queryKey: [resource],
      }),
  });
  const [deleteOpen, setDeleteOpen] = React.useState(false);

  return (
    <>
      <div onClick={(e) => e.stopPropagation()}>
        <DropdownMenu dir={i18n.dir()}>
          <DropdownMenuTrigger asChild>
            <Button
              size="icon"
              variant="ghost"
              className="data-[state=open]:bg-muted"
            >
              <MoreHorizontal className="size-4" />
            </Button>
          </DropdownMenuTrigger>

          <DropdownMenuContent align="end" className="w-48">
            <DropdownMenuLabel>{t("operation")}</DropdownMenuLabel>
            <DropdownMenuSeparator />

            <DropdownMenuItem
              onSelect={() => navigate(`/dashboard/${resource}/${id}/edit`)}
            >
              <Edit className="ml-2 size-4" />
              {t("edit")}
            </DropdownMenuItem>

            <DropdownMenuItem
              onSelect={() => navigate(`/dashboard/${resource}/${id}/show`)}
            >
              <LucideEye className="ml-2 size-4" />
              {t("show")}
            </DropdownMenuItem>

            <DropdownMenuItem
              onSelect={() => setDeleteOpen(true)}
              className="text-destructive focus:text-destructive"
            >
              <Trash2 className="ml-2 size-4" />
              {t("delete")}
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>

        <DeleteConfirmationDialog
          open={deleteOpen}
          onOpenChange={setDeleteOpen}
          title={`#${name}`}
          loading={isPending}
          onConfirm={() => deleteResource(id)}
        />
      </div>
    </>
  );
}
